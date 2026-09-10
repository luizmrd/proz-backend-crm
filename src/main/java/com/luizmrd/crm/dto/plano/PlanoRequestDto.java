package com.luizmrd.crm.dto.plano;


import java.math.BigDecimal;

public record PlanoRequestDto(
        String nome,
        String descricao,
        BigDecimal valorPadrao

){
}
