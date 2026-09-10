package com.luizmrd.crm.service.especificacao;

import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.dto.aula.AulaFiltroRequesDto;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;



public class AulaEspecificacao {

    public static Specification<AulaEntity> comFiltro(AulaFiltroRequesDto filtro) {
        if (filtro == null) {
            return (root, query, cb) -> null;
        }

        return Specification
                .where(dataIgual(filtro.data()))
                .and(horarioDentroDoIntervalo(filtro.dataInicio(), filtro.dataFim()))
                .and(statusIgual(filtro.status()))
                .and(professorContem(filtro.professor()))
                .and(modalidadeIgual(filtro.modalidade()));
    }

    private static Specification<AulaEntity> dataIgual(java.time.LocalDate data) {
        return (root, query, cb) -> {
            if (data == null) {
                return null;
            }
            return cb.equal(root.get("data"), data);
        };
    }

    private static Specification<AulaEntity> horarioDentroDoIntervalo(java.time.LocalTime dataInicio, java.time.LocalTime dataFim) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (dataInicio != null) {

                predicates.add(cb.greaterThanOrEqualTo(root.get("horarioInicio"), dataInicio));
            }

            if (dataFim != null) {

                predicates.add(cb.lessThanOrEqualTo(root.get("horarioFim"), dataFim));
            }

            if (predicates.isEmpty()) {
                return null;
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<AulaEntity> statusIgual(String status) {
        return (root, query, cb) -> {
            if (status == null || status.isBlank()) {
                return null;
            }

            return cb.equal(cb.upper(root.get("status").as(String.class)), status.toUpperCase());
        };
    }

    private static Specification<AulaEntity> professorContem(String professor) {
        return (root, query, cb) -> {
            if (professor == null || professor.isBlank()) {
                return null;
            }

            return cb.like(cb.upper(root.get("professor")), "%" + professor.toUpperCase() + "%");
        };
    }

    private static Specification<AulaEntity> modalidadeIgual(String modalidade) {
        return (root, query, cb) -> {
            if (modalidade == null || modalidade.isBlank()) {
                return null;
            }
            return cb.equal(cb.upper(root.get("modalidade")), modalidade.toUpperCase());
        };
    }



}
