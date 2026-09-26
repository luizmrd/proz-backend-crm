package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoHash;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.ContratoEntity;
import com.luizmrd.crm.database.model.HistoricoPagamentoEntity;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IContratoRepository;
import com.luizmrd.crm.database.repository.IHistoricoPagamentoRepository;
import com.luizmrd.crm.dto.contrato.ContratoAtualizarRequestDto;
import com.luizmrd.crm.dto.contrato.ContratoDto;
import com.luizmrd.crm.exception.BadRequestException;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ContratoService {


    private final IHistoricoPagamentoRepository historicoPagamentoRepository;
    private final IContratoRepository contratoRepository;
    private final IAlunoRepository alunoRepository;
    private final CodigoHash codigoHash;

    public ContratoService(IContratoRepository contratoRepository, IAlunoRepository alunoRepository,
                           CodigoHash codigoHash, IHistoricoPagamentoRepository historicoPagamentoRepository) {
        this.contratoRepository = contratoRepository;
        this.alunoRepository = alunoRepository;
        this.codigoHash = codigoHash;
        this.historicoPagamentoRepository = historicoPagamentoRepository;
    }

    @Transactional
    public void criarContrato(ContratoDto contratoDto) {

        AlunoEntity aluno = alunoRepository.findById(contratoDto.aluno())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (contratoRepository.findByAlunoId(contratoDto.aluno()).isPresent()) {
            throw new BadRequestException("O aluno já possui um contrato");
        }

        String assinaturaDigital = codigoHash.gerarHash(contratoDto.assinaturaDigital());

       ContratoEntity contrato =  contratoRepository.save(
                ContratoEntity.builder()
                        .aluno(aluno)
                        .aceiteContrato(contratoDto.aceiteContrato())
                        .termosContrato(contratoDto.termosContrato())
                        .assinaturaDigital(assinaturaDigital)
                        .diaVencimentoMensalidade(contratoDto.diaVencimentoMensalidade())
                        .aceitoEm(java.time.LocalDateTime.now())
                        .build()

        );
        aluno.setDiaVencimento(contrato.getDiaVencimentoMensalidade());
        aluno.setContrato(contrato);



        HistoricoPagamentoEntity historicoPagamento = HistoricoPagamentoEntity.builder()
                .aluno(aluno)
                .dataPagamento(LocalDateTime.now())
                .valor(aluno.getValorMensal())
                .statusPagamento(StatusPagamentoEnum.PAGO)
                .recibo("#REC-" + System.currentTimeMillis())
                .build();

        historicoPagamentoRepository.save(historicoPagamento);

    }

    public Page<ContratoEntity> listarContratos(Pageable pageable) {
        return contratoRepository.findAll(pageable);
    }

    public ContratoEntity buscarContrato(Long id) {
        return contratoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado"));
    }

    public ContratoEntity buscarContratoPorAluno(Long alunoId) {
        return contratoRepository.findByAlunoId(alunoId)
                .orElseThrow(() -> new ResourceNotFoundException("Contrato não encontrado para o aluno"));
    }

    @Transactional
    public ContratoEntity atualizarContrato(Long id, ContratoAtualizarRequestDto contratoAtualizarRequestDto) {
        ContratoEntity contrato = buscarContrato(id);

        if (contratoAtualizarRequestDto.aceiteContrato() != null) {
            contrato.setAceiteContrato(contratoAtualizarRequestDto.aceiteContrato());
        }
        if (contratoAtualizarRequestDto.termosContrato() != null) {
            contrato.setTermosContrato(contratoAtualizarRequestDto.termosContrato());
        }
        if (contratoAtualizarRequestDto.diaVencimentoMensalidade() != null) {
            contrato.setDiaVencimentoMensalidade(contratoAtualizarRequestDto.diaVencimentoMensalidade());
            if (contrato.getAluno() != null) {
                contrato.getAluno().setDiaVencimento(contratoAtualizarRequestDto.diaVencimentoMensalidade());
            }
        }
        if (contratoAtualizarRequestDto.assinaturaDigital() != null) {
            contrato.setAssinaturaDigital(codigoHash.gerarHash(contratoAtualizarRequestDto.assinaturaDigital()));
        }

        return contratoRepository.save(contrato);
    }

    @Transactional
    public void excluirContrato(Long id) {
        ContratoEntity contrato = buscarContrato(id);

        if (contrato.getAluno() != null) {
            contrato.getAluno().setContrato(null);
        }

        contratoRepository.delete(contrato);
    }

}
