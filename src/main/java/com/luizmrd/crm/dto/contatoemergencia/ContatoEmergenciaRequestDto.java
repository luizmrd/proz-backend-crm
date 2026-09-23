package com.luizmrd.crm.dto.contatoemergencia;

public record ContatoEmergenciaRequestDto(
        Long alunoId,
        String nome,
        String telefone,
        String parentesco
) {
}
