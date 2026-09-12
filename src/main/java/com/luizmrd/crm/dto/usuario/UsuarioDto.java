package com.luizmrd.crm.dto.usuario;

import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;

public record UsuarioDto(
        String nome,
        String email,
        String senha,
        PerfilAcessoEnum cargo,
        String telefone
) {
}
