package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.enuns.MetodoPagamentoEnum;
import jakarta.validation.constraints.NotNull;

public record RecebimentoQuitarRequestDto(
        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId,
        @NotNull(message = "O método de pagamento é obrigatório")
        MetodoPagamentoEnum metodoPagamento
) {
}
