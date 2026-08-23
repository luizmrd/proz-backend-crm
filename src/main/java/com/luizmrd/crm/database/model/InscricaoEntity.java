package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.PresencaStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "inscricao")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class InscricaoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false,name = "presenca_status")
    private PresencaStatus presencaStatus;
    @Column(nullable = false,name = "data_inscricao")
    private LocalDate dataInscricao;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private AlunoEntity aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aula_id", nullable = false)
    private AulaEntity aula;




}
