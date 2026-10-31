package com.luizmrd.crm.dto.financeiro;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RecebimentoRequestDto(
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor,
        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId
) {
}
