package com.luizmrd.crm.dto.contatoemergencia;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContatoEmergenciaRequestDto(
        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId,
        @NotBlank(message = "O nome do contato é obrigatório")
        String nome,
        @NotBlank(message = "O telefone do contato é obrigatório")
        String telefone,
        @NotBlank(message = "O parentesco é obrigatório")
        String parentesco
) {
}
