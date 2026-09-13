package com.luizmrd.crm.dto.usuario;

import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;

public record UsuarioFiltroDto(
        String nome,
        PerfilAcessoEnum cargo,
        Boolean ativo
) {
}
