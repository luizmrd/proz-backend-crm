package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

public record RecebimentoFiltroDto(
        StatusPagamentoEnum status,
        Long alunoId,
        String referencia
) {
}
