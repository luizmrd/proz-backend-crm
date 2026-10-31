package com.luizmrd.crm.dto.plano;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PlanoRequestDto(
        @NotBlank(message = "O nome do plano é obrigatório")
        String nome,
        String descricao,
        @NotNull(message = "O valor padrão é obrigatório")
        @Positive(message = "O valor padrão deve ser maior que zero")
        BigDecimal valorPadrao

){
}
