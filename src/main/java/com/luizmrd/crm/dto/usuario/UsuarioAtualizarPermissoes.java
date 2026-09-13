package com.luizmrd.crm.dto.usuario;

import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;

public record UsuarioAtualizarPermissoes(
        String senha,
        PerfilAcessoEnum cargo
) {
}
