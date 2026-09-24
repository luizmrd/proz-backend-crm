package com.luizmrd.crm.dto.contatoemergencia;

import com.luizmrd.crm.database.model.ContatoEmergenciaEntity;

public record ContatoEmergenciaResponseDto(
        Long id,
        Long alunoId,
        String nome,
        String telefone,
        String parentesco
) {

    public static ContatoEmergenciaResponseDto de(ContatoEmergenciaEntity contato) {
        return new ContatoEmergenciaResponseDto(
                contato.getId(),
                contato.getAluno() != null ? contato.getAluno().getId() : null,
                contato.getNome(),
                contato.getTelefone(),
                contato.getParentesco()
        );
    }
}
