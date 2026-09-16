package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.CategoriaContasEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Table(name = "contas_pagar")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ContasPagarEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;
    @Column(nullable = false)

    @Enumerated(EnumType.STRING)
    private CategoriaContasEnum categoria;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(name = "data_vencimento")
    private LocalDate dataVencimento;

    @Column(name = "data_pagamento")
    private LocalDate dataPagamento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusPagamentoEnum statusPagamento;

    private String comprovate;
}
