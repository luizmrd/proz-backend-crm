package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.ContatoEmergenciaEntity;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IContatoEmergenciaRepository;
import com.luizmrd.crm.dto.contatoemergencia.ContatoEmergenciaAtualizarRequestDto;
import com.luizmrd.crm.dto.contatoemergencia.ContatoEmergenciaRequestDto;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class ContatoEmergenciaService {

    private final IContatoEmergenciaRepository contatoEmergenciaRepository;
    private final IAlunoRepository alunoRepository;

    public ContatoEmergenciaService(IContatoEmergenciaRepository contatoEmergenciaRepository,
                                    IAlunoRepository alunoRepository) {
        this.contatoEmergenciaRepository = contatoEmergenciaRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void criarContatoEmergencia(ContatoEmergenciaRequestDto contatoEmergencia) {

        AlunoEntity aluno = alunoRepository.findById(contatoEmergencia.alunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        ContatoEmergenciaEntity contatoEmergenciaEntity = contatoEmergenciaRepository.save(
                ContatoEmergenciaEntity.builder()
                        .aluno(aluno)
                        .nome(contatoEmergencia.nome())
                        .telefone(contatoEmergencia.telefone())
                        .parentesco(contatoEmergencia.parentesco())
                        .build()
        );

        aluno.setContatoEmergencia(contatoEmergenciaEntity);
    }

    public ContatoEmergenciaEntity buscarContatoEmergencia(Long id) {
        return contatoEmergenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contato de emergência não encontrado"));
    }

    @Transactional
    public ContatoEmergenciaEntity atualizarContatoEmergencia(Long id,
                                                              ContatoEmergenciaAtualizarRequestDto contatoEmergencia) {
        ContatoEmergenciaEntity contatoEmergenciaEntity = buscarContatoEmergencia(id);

        if (contatoEmergencia.nome() != null) {
            contatoEmergenciaEntity.setNome(contatoEmergencia.nome());
        }
        if (contatoEmergencia.telefone() != null) {
            contatoEmergenciaEntity.setTelefone(contatoEmergencia.telefone());
        }
        if (contatoEmergencia.parentesco() != null) {
            contatoEmergenciaEntity.setParentesco(contatoEmergencia.parentesco());
        }

        return contatoEmergenciaRepository.save(contatoEmergenciaEntity);
    }

}
