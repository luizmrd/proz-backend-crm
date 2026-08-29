package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.HistoricoPagamentoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHistoricoPagamentoEntity extends JpaRepository<HistoricoPagamentoEntity, Long> {

}
