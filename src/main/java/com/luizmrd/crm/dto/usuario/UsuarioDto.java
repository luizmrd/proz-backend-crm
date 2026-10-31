package com.luizmrd.crm.dto.usuario;

import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDto(
        @NotBlank(message = "O nome é obrigatório")
        String nome,
        @NotBlank(message = "O e-mail é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        String senha,
        @NotNull(message = "O cargo é obrigatório")
        PerfilAcessoEnum cargo,
        String telefone
) {
}
