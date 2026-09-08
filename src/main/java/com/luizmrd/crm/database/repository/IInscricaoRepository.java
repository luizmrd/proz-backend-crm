package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.database.model.InscricaoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IInscricaoRepository extends JpaRepository<InscricaoEntity, Long> {


    Optional<InscricaoEntity> findByAulaAndAluno(AulaEntity aula, AlunoEntity aluno);

    Optional<InscricaoEntity> findByAulaIdAndAlunoId(Long aulaId, Long alunoId);
}
