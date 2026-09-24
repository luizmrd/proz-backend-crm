package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.EnderecoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IEnderecoRepository extends JpaRepository<EnderecoEntity, Long> {

    Optional<EnderecoEntity> findByAlunoId(Long alunoId);

}
