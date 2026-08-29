package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AlunoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IAlunoRepository extends JpaRepository<AlunoEntity, Long>, JpaSpecificationExecutor<AlunoEntity> {

}
