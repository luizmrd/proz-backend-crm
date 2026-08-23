package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.CategoriaContas;
import com.luizmrd.crm.database.model.enuns.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Table(name = "contas_pagar")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContasPagarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String descricao;
    @Column(nullable = false)
    private CategoriaContas categoria;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false, name = "data_vencimento")
    private LocalDate dataVencimento;
    @Column(nullable = false)
    private Status status;
}
