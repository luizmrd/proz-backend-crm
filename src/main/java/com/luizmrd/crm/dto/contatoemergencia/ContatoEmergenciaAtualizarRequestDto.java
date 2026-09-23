package com.luizmrd.crm.dto.contatoemergencia;

public record ContatoEmergenciaAtualizarRequestDto(
        String nome,
        String telefone,
        String parentesco
) {
}
