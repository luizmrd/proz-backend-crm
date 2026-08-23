package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.Status;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "historico_pagamento")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class HistoricoPagamentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id",nullable = false)
    private AlunoEntity aluno;

    @Column(nullable = false, name = "data_pagamento")
    private LocalDateTime dataPagamento;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false)
    private Status status;
    @Column(nullable = false)
    private String recibo;


}
