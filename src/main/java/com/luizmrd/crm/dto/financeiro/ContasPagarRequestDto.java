package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.enuns.CategoriaContasEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasPagarRequestDto(
        @NotNull(message = "O valor é obrigatório")
        @Positive(message = "O valor deve ser maior que zero")
        BigDecimal valor,
        @NotBlank(message = "A descrição é obrigatória")
        String descricao,
        @NotNull(message = "A data de vencimento é obrigatória")
        LocalDate dataVencimento,
        @NotNull(message = "A categoria é obrigatória")
        CategoriaContasEnum categoria,
        StatusPagamentoEnum status
) {
}
