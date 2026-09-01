package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.PlanoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPlanoRepository extends JpaRepository<PlanoEntity, Long> {

    Optional<PlanoEntity> findByNome(String nome);
}
