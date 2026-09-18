package com.luizmrd.crm.dto.auth;

import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;

public record UsuarioAutenticadoDto(
        Long id,
        String nome,
        String email,
        PerfilAcessoEnum cargo
) {

    public static UsuarioAutenticadoDto de(UsuarioEntity usuario) {
        return new UsuarioAutenticadoDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCargo()
        );
    }
}
