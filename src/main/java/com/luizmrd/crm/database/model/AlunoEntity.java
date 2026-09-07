package com.luizmrd.crm.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.luizmrd.crm.database.model.enuns.SexoEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Table(name = "aluno")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AlunoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false,unique = true)
    private String cpf;
    @Column(nullable = false,name = "data_nascimento")
    private LocalDate dataNascimento;
    @Column(nullable = false)
    private String telefone;
    @Column(nullable = false)
    private String email;

    private SexoEnum sexo;
    @Column(unique = true)
    private String codigoAcesso;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatusEnum statusEnum = StatusEnum.ATIVO;

    @ManyToOne
    @JoinColumn(name = "plano_id")
    private PlanoEntity plano;

    @Column(nullable = false)
    private BigDecimal valorMensal;

    private Integer diaVencimento;

    private StatusPagamentoEnum statusPagamento;
    @JsonIgnore
    @OneToOne
    @JoinColumn(name = "contrato_id")
    private ContratoEntity contrato;
    @JsonIgnore
    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoricoPagamentoEntity> historicoPagamento = new ArrayList<>();


    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InscricaoEntity> inscricoes = new ArrayList<>();



}
