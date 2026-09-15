package com.luizmrd.crm.dto.financeiro;

import java.math.BigDecimal;

public record RecebimentoRequestDto(

        BigDecimal valor,
        Long alunoId
) {
}
