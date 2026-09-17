package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface IRecebimentoRepository extends JpaRepository<RecebimentoEntity, Long> , JpaSpecificationExecutor<RecebimentoEntity> {


    @Query("""
    SELECT COALESCE(SUM(r.valor), 0) 
    FROM RecebimentoEntity r 
    WHERE r.statusPagamento = :status 
      AND r.dataVencimento BETWEEN :inicio AND :fim
""")
    BigDecimal somarPorStatusEIntervalo(
            @Param("status") StatusPagamentoEnum status,
            @Param("inicio") LocalDate inicio,
            @Param("fim") LocalDate fim
    );

    List<RecebimentoEntity> findByStatusPagamento(StatusPagamentoEnum status);
}
