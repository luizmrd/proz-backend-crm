package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.ContratoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IContratoRepository extends JpaRepository<ContratoEntity, Long> {

    Optional<ContratoEntity> findByAlunoId(Long alunoId);

}
