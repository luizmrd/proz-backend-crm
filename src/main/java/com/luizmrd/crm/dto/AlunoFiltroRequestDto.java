package com.luizmrd.crm.dto;

public record AlunoFiltroRequestDto(
        String status,
        String statusPagamento,
        Boolean emRiscoEvasao
) {
}
