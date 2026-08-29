package com.luizmrd.crm.dto;

public record AlunoFiltroRequest(
        String status,
        String statusPagamento,
        Boolean emRiscoEvasao
) {
}
