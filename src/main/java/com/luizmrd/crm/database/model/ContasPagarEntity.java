package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.CategoriaContasEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
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
    private CategoriaContasEnum categoria;
    @Column(nullable = false)
    private BigDecimal valor;
    @Column(nullable = false, name = "data_vencimento")
    private LocalDate dataVencimento;
    @Column(nullable = false)
    private StatusEnum statusEnum;
}
