package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoHash;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.ContratoEntity;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IContratoRepository;
import com.luizmrd.crm.dto.ContratoDto;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ContratoService {


    private final IContratoRepository contratoRepository;
    private final IAlunoRepository alunoRepository;
    private final CodigoHash codigoHash;

    public ContratoService( IContratoRepository contratoRepository, IAlunoRepository alunoRepository, CodigoHash codigoHash) {
        this.contratoRepository = contratoRepository;
        this.alunoRepository = alunoRepository;
        this.codigoHash = codigoHash;
    }

    @Transactional
    public void criarContrato(ContratoDto contratoDto) {

        AlunoEntity aluno = alunoRepository.findById(contratoDto.aluno())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

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

    }

}
