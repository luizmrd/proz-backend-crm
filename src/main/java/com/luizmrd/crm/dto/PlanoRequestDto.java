package com.luizmrd.crm.dto;


import java.math.BigDecimal;

public record PlanoRequestDto(
        String nome,
        String descricao,
        BigDecimal valorPadrao

){
}
