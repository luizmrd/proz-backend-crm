package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface IAlunoRepository extends JpaRepository<AlunoEntity, Long>, JpaSpecificationExecutor<AlunoEntity> {


    boolean existsByCodigoAcesso(String codigoAcesso);
    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);

    List<AlunoEntity> findByStatusEnum(StatusEnum statusEnum);

}
