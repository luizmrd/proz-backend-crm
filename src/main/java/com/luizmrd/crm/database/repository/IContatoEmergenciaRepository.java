package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.ContatoEmergenciaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IContatoEmergenciaRepository extends JpaRepository<ContatoEmergenciaEntity, Long> {

    Optional<ContatoEmergenciaEntity> findByAlunoId(Long alunoId);

}
