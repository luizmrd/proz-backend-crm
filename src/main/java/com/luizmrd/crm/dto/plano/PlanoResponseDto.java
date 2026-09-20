package com.luizmrd.crm.dto.plano;

import com.luizmrd.crm.database.model.PlanoEntity;

import java.math.BigDecimal;

public record PlanoResponseDto(
        Long id,
        String nome,
        String descricao,
        BigDecimal valorPadrao,
        Boolean ativo
) {

    public static PlanoResponseDto de(PlanoEntity plano) {
        return new PlanoResponseDto(
                plano.getId(),
                plano.getNome(),
                plano.getDescricao(),
                plano.getValorPadrao(),
                plano.getAtivo()
        );
    }
}
