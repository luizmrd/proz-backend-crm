package com.luizmrd.crm.service.especificacao;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.dto.aluno.AlunoFiltroRequestDto;
import org.springframework.data.jpa.domain.Specification;


public class AlunoEspecificacao {

    public static Specification<AlunoEntity> comFiltro(AlunoFiltroRequestDto filtro){
        return Specification
                .where(statusIgual(filtro.status()))
                .and(statusPagamentoIgual(filtro.statusPagamento()));
    }
    private static Specification<AlunoEntity> statusIgual(String status) {
        return(root, query, cb) -> {
            if (status == null || status.isBlank()) {
                return null;
            }
            return cb.equal(root.get("statusEnum"), StatusEnum.valueOf(status.toUpperCase()));
        };
    };
    private static Specification<AlunoEntity> statusPagamentoIgual(String statusPagamento) {
        return(root, query, cb) -> {
            if (statusPagamento == null || statusPagamento.isBlank()) {
                return null;
            }
            return cb.equal(root.get("statusPagamento"), StatusPagamentoEnum.valueOf(statusPagamento.toUpperCase()));
        };
    };


}
