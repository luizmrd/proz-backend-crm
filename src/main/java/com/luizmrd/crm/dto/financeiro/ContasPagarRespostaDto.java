package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.ContasPagarEntity;
import com.luizmrd.crm.database.model.enuns.CategoriaContasEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContasPagarRespostaDto(
        Long id,
        CategoriaContasEnum categoria,
        LocalDate dataVencimento,
        String descricao,
        BigDecimal valor
) {

    public static ContasPagarRespostaDto de(ContasPagarEntity contas){
        if (contas == null) return null;

        return new ContasPagarRespostaDto(
          contas.getId(),
          contas.getCategoria(),
                contas.getDataVencimento(),
                contas.getDescricao(),
                contas.getValor()
        );
    }
}
