package com.luizmrd.crm.dto.plano;

import java.math.BigDecimal;

public record PlanoAtualizarRequestDto(
        String nome,
        String descricao,
        BigDecimal valorPadrao,
        Boolean ativo
) {
}
