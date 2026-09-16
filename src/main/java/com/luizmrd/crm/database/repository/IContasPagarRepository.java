package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.ContasPagarEntity;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IContasPagarRepository extends JpaRepository<ContasPagarEntity, Long> {


    @Query("""
    SELECT COALESCE(SUM(r.valor), 0) 
    FROM ContasPagarEntity r 
    WHERE r.statusPagamento = :status 
      AND r.dataVencimento BETWEEN :inicio AND :fim
""")
    BigDecimal somarPorStatusEIntervalo(
            @Param("status") StatusPagamentoEnum status,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );
}
