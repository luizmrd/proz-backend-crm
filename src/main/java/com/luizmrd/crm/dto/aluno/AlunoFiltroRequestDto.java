package com.luizmrd.crm.dto.aluno;

public record AlunoFiltroRequestDto(
        String status,
        String statusPagamento,
        Boolean emRiscoEvasao
) {
}
