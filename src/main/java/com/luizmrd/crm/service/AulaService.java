package com.luizmrd.crm.service;


import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.database.model.enuns.StatusAula;
import com.luizmrd.crm.database.repository.IAulaRepository;
import com.luizmrd.crm.dto.AulaRequestDto;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AulaService {

    private final IAulaRepository aulaRepository;

    public AulaService(IAulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    public void criarAula(AulaRequestDto aula){
        aulaRepository.save(
                AulaEntity.builder()
                        .modalidade(aula.modalidade())
                        .data(aula.data())
                        .dataInicio(aula.dataInicio())
                        .dataFim(aula.dataFim())
                        .professor(aula.professor())
                        .limiteVagas(aula.limiteVagas())
                        .sala(aula.sala())
                        .build()
        );
    }

    public AulaEntity bucarAulaPorId(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada"));
    }

    public void atualizarAula(Long id, AulaRequestDto aula) {
        AulaEntity aulaEntity = bucarAulaPorId(id);
        aulaEntity.setModalidade(aula.modalidade());
        aulaEntity.setData(aula.data());
        aulaEntity.setDataInicio(aula.dataInicio());
        aulaEntity.setDataFim(aula.dataFim());
        aulaEntity.setProfessor(aula.professor());
        aulaEntity.setLimiteVagas(aula.limiteVagas());
        aulaEntity.setSala(aula.sala());
        aulaRepository.save(aulaEntity);
    }

    public void cancelarAula(Long id) {
        AulaEntity aulaEntity = aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada"));
        aulaEntity.setStatusAula(StatusAula.CANCELADA);
        aulaRepository.save(aulaEntity);
    }

}
