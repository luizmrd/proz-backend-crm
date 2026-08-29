package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.ContasPagarEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IContasPagarRepository extends JpaRepository<ContasPagarEntity, Long> {

}
