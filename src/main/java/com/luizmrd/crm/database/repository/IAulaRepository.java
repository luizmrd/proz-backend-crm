package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AulaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IAulaRepository extends JpaRepository<AulaEntity, Long>, JpaSpecificationExecutor<AulaEntity> {


}
