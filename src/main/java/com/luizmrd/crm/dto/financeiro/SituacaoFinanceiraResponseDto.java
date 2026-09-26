package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SituacaoFinanceiraResponseDto(
        Long alunoId,
        String alunoNome,
        String planoAtual,
        BigDecimal valorMensal,
        LocalDate proximoVencimento,
        StatusPagamentoEnum situacaoPlano
) {
}
