    package com.luizmrd.crm.dto.usuario;

    import com.luizmrd.crm.database.model.UsuarioEntity;
    import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;

    public record UsuarioResumoDto(
            String nome,
            String email,
            String telefone,
            PerfilAcessoEnum cargo,
            Boolean ativo

    ) {

        public static UsuarioResumoDto de(UsuarioEntity usuario) {
            return new UsuarioResumoDto(
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getTelefone(),
                    usuario.getCargo(),
                    usuario.isAtivo()
            );
        }
    }
