package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.enuns.MetodoPagamentoEnum;

public record RecebimentoQuitarRequestDto(
        Long alunoId,
        MetodoPagamentoEnum metodoPagamento
) {
}
