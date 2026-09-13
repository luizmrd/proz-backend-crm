package com.luizmrd.crm.dto.recebimento;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.enuns.MetodoPagamentoEnum;

import java.math.BigDecimal;

public record RecebimentoRequestDto(

        BigDecimal valor,
        Long alunoId
) {
}
