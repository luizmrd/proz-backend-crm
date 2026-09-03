package com.luizmrd.crm.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "contrato")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ContratoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private AlunoEntity aluno;
    @Column(nullable = false,name = "aceite_contrato")
    private Boolean aceiteContrato;

    @Column(nullable = false,name = "termos_contrato", length = 10000)
    private String termosContrato;

    private Integer diaVencimentoMensalidade;

    @Column(nullable = false,name = "assinatura_digital")
    private String assinaturaDigital;
    @Column(nullable = false,name = "aceito_em")
    private LocalDateTime aceitoEm;


}
