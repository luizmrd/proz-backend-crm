package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.database.model.InscricaoEntity;
import com.luizmrd.crm.database.model.enuns.PresencaStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface IInscricaoRepository extends JpaRepository<InscricaoEntity, Long> {


    Optional<InscricaoEntity> findByAulaAndAluno(AulaEntity aula, AlunoEntity aluno);

    Optional<InscricaoEntity> findByAulaIdAndAlunoId(Long aulaId, Long alunoId);

    long countByAlunoId(Long alunoId);

    @Query("""
            SELECT MAX(a.data)
            FROM InscricaoEntity i
            JOIN i.aula a
            WHERE i.aluno.id = :alunoId
              AND i.presencaStatus = :status
            """)
    LocalDate buscarUltimaPresenca(@Param("alunoId") Long alunoId,
                                   @Param("status") PresencaStatusEnum status);
}
