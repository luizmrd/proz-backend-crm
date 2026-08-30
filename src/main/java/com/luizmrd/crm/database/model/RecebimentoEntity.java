package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.MetodoPagamentoEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false, name = "data_vencimento")
    private LocalDateTime dataVencimento;
    @Column(nullable = false, name = "data_pagamento")
    private LocalDateTime dataPagamento;

    @Column(nullable = false,name = "metodo_pagamento")
    private MetodoPagamentoEnum metodoPagamento;
    @Column(nullable = false)
    private StatusEnum statusEnum;
    @Column(nullable = false)
    private String recibo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private AlunoEntity aluno;

}
