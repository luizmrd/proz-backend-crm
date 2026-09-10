package com.luizmrd.crm.service.especificacao;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.dto.aluno.AlunoFiltroRequestDto;
import org.springframework.data.jpa.domain.Specification;


public class AlunoEspecificacao {

    public static Specification<AlunoEntity> comFiltro(AlunoFiltroRequestDto filtro){
        return Specification
                .where(statusIgual(filtro.status()))
                .and(statusPagamentoIgual(filtro.statusPagamento()))
                .and(emRiscoEvasaoIgual(filtro.emRiscoEvasao()));
    }
    private static Specification<AlunoEntity> statusIgual(String status) {
        return(root, query, cb) -> {
            if (status == null || status.isBlank()) {
                return null;
            }
            return cb.equal(cb.upper(root.get("status")),status.toUpperCase());
        };
    };
    private static Specification<AlunoEntity> statusPagamentoIgual(String statusPagamento) {
        return(root, query, cb) -> {
            if (statusPagamento == null || statusPagamento.isBlank()) {
                return null;
            }
            return cb.equal(cb.upper(root.get("status")),statusPagamento.toUpperCase());
        };
    };
    private static Specification<AlunoEntity> emRiscoEvasaoIgual(Boolean emRiscoEvasao) {
        return(root, query, cb) -> {
            if (emRiscoEvasao == null) {
                return null;
            }
            return cb.equal(root.get("emRiscoEvasao"), emRiscoEvasao);
        };
    };


}
