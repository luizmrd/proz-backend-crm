package com.luizmrd.crm.database.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.luizmrd.crm.database.model.enuns.StatusAula;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Table(name = "aula")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AulaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String modalidade;
    private LocalDate data;
    @Column(name = "data_inicio")
    private LocalTime dataInicio;
    @Column(name = "data_fim")
    private LocalTime dataFim;
    private String professor;
    @Column(name = "limite_vagas")
    private Integer limiteVagas;
    private String sala;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private StatusAula statusAula = StatusAula.AGENDADA;

    @JsonIgnoreProperties({"cpf", "nascimento", "sexo", "telefone", "email", "endereco", "historicoPagamentos", "historicoAulas", "contrato", "plano"})
    @OneToMany(mappedBy = "aula", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InscricaoEntity> inscricoes = new ArrayList<>();

}
