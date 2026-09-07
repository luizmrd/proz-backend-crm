package com.luizmrd.crm.database.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "aula")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AulaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

}
