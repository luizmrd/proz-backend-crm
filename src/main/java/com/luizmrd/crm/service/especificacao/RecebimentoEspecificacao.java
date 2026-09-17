package com.luizmrd.crm.service.especificacao;

import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.dto.financeiro.RecebimentoFiltroDto;
import org.springframework.data.jpa.domain.Specification;

public class RecebimentoEspecificacao {

    public static Specification<RecebimentoEntity> comFiltro(RecebimentoFiltroDto filtro) {
        if (filtro == null) {
            return (root, query, cb) -> null;
        }

        return Specification
                .where(statusIgual(filtro.status()))
                .and(alunoIdIgual(filtro.alunoId()))
                .and(referenciaContem(filtro.referencia()));
    }

    private static Specification<RecebimentoEntity> statusIgual(StatusPagamentoEnum status) {
        return (root, query, cb) -> {
            if (status == null) {
                return null;
            }
            return cb.equal(root.get("statusPagamento"), status);
        };
    }

    private static Specification<RecebimentoEntity> alunoIdIgual(Long alunoId) {
        return (root, query, cb) -> {
            if (alunoId == null) {
                return null;
            }
            return cb.equal(root.get("aluno").get("id"), alunoId);
        };
    }

    private static Specification<RecebimentoEntity> referenciaContem(String referencia) {
        return (root, query, cb) -> {
            if (referencia == null || referencia.isBlank()) {
                return null;
            }
            return cb.like(cb.upper(root.get("referencia")), "%" + referencia.toUpperCase() + "%");
        };
    }
}
