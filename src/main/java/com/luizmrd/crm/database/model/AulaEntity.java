package com.luizmrd.crm.database.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Table(name = "aula")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
