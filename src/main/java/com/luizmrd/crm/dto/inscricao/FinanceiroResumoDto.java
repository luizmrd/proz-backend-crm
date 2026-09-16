package com.luizmrd.crm.dto.inscricao;

import java.math.BigDecimal;

public record FinanceiroResumoDto(

        BigDecimal totalRecebido,
        BigDecimal totalPendente,
        BigDecimal totalDespesas,
        BigDecimal saldoLiquido
) {
}
