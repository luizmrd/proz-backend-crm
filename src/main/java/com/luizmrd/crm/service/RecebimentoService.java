package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.ContratoEntity;
import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IContratoRepository;
import com.luizmrd.crm.database.repository.IRecebimentoRepository;
import com.luizmrd.crm.dto.recebimento.RecebimentoRequestDto;
import com.luizmrd.crm.util.DiaVencimentoUtil;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class RecebimentoService {


    private final IRecebimentoRepository recebimentoRepository;
    private final IAlunoRepository alunoRepository;
    private final IContratoRepository contratoRepository;


    public RecebimentoService(IRecebimentoRepository recebimentoRepository, IAlunoRepository alunoRepository,
                              IContratoRepository contratoRepository) {
        this.contratoRepository = contratoRepository;
        this.alunoRepository = alunoRepository;
        this.recebimentoRepository = recebimentoRepository;
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


}
