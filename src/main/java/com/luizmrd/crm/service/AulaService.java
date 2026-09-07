package com.luizmrd.crm.service;


import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.database.repository.IAulaRepository;
import com.luizmrd.crm.dto.AulaRequestDto;
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

}
