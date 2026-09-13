package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.MetodoPagamentoEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "recebimento")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecebimentoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false, name = "data_vencimento")
    private LocalDate dataVencimento;
    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(name = "metodo_pagamento")
    private MetodoPagamentoEnum metodoPagamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPagamentoEnum statusPagamento;

    private String recibo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private AlunoEntity aluno;

}
