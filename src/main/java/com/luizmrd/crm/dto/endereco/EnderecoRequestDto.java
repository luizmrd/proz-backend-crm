package com.luizmrd.crm.dto.endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EnderecoRequestDto(
        @NotNull(message = "O ID do aluno é obrigatório")
        Long alunoId,
        @NotBlank(message = "A rua é obrigatória")
        String rua,
        @NotBlank(message = "A cidade é obrigatória")
        String cidade,
        @NotNull(message = "O número é obrigatório")
        Integer numero,
        @NotBlank(message = "O CEP é obrigatório")
        String cep,
        @NotBlank(message = "A UF é obrigatória")
        String uf,
        String complemento
) {
}
