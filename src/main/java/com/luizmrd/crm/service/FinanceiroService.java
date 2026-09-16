package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.ContasPagarEntity;
import com.luizmrd.crm.database.model.ContratoEntity;
import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IContasPagarRepository;
import com.luizmrd.crm.database.repository.IContratoRepository;
import com.luizmrd.crm.database.repository.IRecebimentoRepository;
import com.luizmrd.crm.dto.financeiro.ContasPagarRequestDto;
import com.luizmrd.crm.dto.financeiro.RecebimentoQuitadoResponseDto;
import com.luizmrd.crm.dto.financeiro.RecebimentoQuitarRequestDto;
import com.luizmrd.crm.dto.financeiro.RecebimentoRequestDto;
import com.luizmrd.crm.exception.BadRequestException;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import com.luizmrd.crm.util.DiaVencimentoUtil;
import com.luizmrd.crm.util.ReciboUtil;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class FinanceiroService {


    private final IRecebimentoRepository recebimentoRepository;
    private final IAlunoRepository alunoRepository;
    private final IContratoRepository contratoRepository;
    private final IContasPagarRepository contasPagarRepository;


    public FinanceiroService(IRecebimentoRepository recebimentoRepository, IAlunoRepository alunoRepository,
                             IContratoRepository contratoRepository, IContasPagarRepository contasPagarRepository) {
        this.contratoRepository = contratoRepository;
        this.alunoRepository = alunoRepository;
        this.recebimentoRepository = recebimentoRepository;
        this.contasPagarRepository = contasPagarRepository;
    }

    public void criarRecebimento(RecebimentoRequestDto recebimento){
        AlunoEntity aluno = alunoRepository.findById(recebimento.alunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        ContratoEntity contrato = contratoRepository.findByAlunoId(aluno.getId())
                .orElseThrow(() -> new RuntimeException("Contrato não encontrado"));

        Integer dia = contrato.getDiaVencimentoMensalidade();

        LocalDate diaVencimento = DiaVencimentoUtil.montarDataVencimento(dia);


        recebimentoRepository.save(RecebimentoEntity.builder()
                        .aluno(aluno)
                        .dataVencimento(diaVencimento)
                        .statusPagamento(StatusPagamentoEnum.PENDENTE)
                        .valor(recebimento.valor())
                        .build()

                );

    }

    @Transactional
    public RecebimentoQuitadoResponseDto quitarRecebimento(Long recebimentoId, RecebimentoQuitarRequestDto recebimentoRequest) throws BadRequestException {

        RecebimentoEntity recebimento = recebimentoRepository.findById(recebimentoId)
                .orElseThrow(() -> new ResourceNotFoundException("Recebimento não encontrado"));

        AlunoEntity aluno = alunoRepository.findById(recebimentoRequest.alunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));


        if (StatusPagamentoEnum.PAGO.equals(recebimento.getStatusPagamento())){
            throw new BadRequestException("Recebimento já pago");
        }


       recebimento.setStatusPagamento(StatusPagamentoEnum.PAGO);
        recebimento.setDataPagamento(LocalDateTime.now());
        recebimento.setMetodoPagamento(recebimentoRequest.metodoPagamento());
        recebimento.setRecibo(ReciboUtil.gerarCodigoRecibo());

        AlunoEntity alunoSinc = recebimento.getAluno();
        if (alunoSinc != null) {
            aluno.setStatusPagamento(StatusPagamentoEnum.PAGO);
        }

        return RecebimentoQuitadoResponseDto.de(recebimentoRepository.save(recebimento));

    }

    public void criarContasPagar(ContasPagarRequestDto contas){

        contasPagarRepository.save(ContasPagarEntity.builder()
                        .valor(contas.valor())
                        .descricao(contas.descricao())
                        .dataVencimento(contas.dataVencimento())
                        .categoria(contas.categoria())
                        .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .build()
        );
    }

    public void quitarContasPagar(Long id){

        ContasPagarEntity contas = contasPagarRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Conta não encontrado"));

        if (StatusPagamentoEnum.PAGO.equals(contas.getStatusPagamento())){
            throw new BadRequestException("Conta já pago");
        }

      contas.setDataPagamento(LocalDate.now());
        contas.setComprovate(ReciboUtil.gerarCodigoComprovante());
        contas.setStatusPagamento(StatusPagamentoEnum.PAGO);

        contasPagarRepository.save(contas);

    }


}
