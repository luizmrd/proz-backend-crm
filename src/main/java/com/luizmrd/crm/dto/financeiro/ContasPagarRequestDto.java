package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.enuns.CategoriaContasEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasPagarRequestDto(
        BigDecimal valor,
        String descricao,
        LocalDate dataVencimento,
        CategoriaContasEnum categoria,
        StatusPagamentoEnum status
) {
}
