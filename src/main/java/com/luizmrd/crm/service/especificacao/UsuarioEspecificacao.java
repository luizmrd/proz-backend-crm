package com.luizmrd.crm.service.especificacao;

import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.dto.usuario.UsuarioFiltroDto;
import org.springframework.data.jpa.domain.Specification;

public class UsuarioEspecificacao {


    public static Specification<UsuarioEntity> comFiltro(UsuarioFiltroDto filtro) {
        if (filtro == null) {
            return (root, query, cb) -> null;
        }

        return Specification
                .where(nomeContem(filtro.nome()))
                .and(perfilAcessoContem(filtro.cargo()))
                .and(ativoIgual(filtro.ativo()));
    }

    private static Specification<UsuarioEntity> nomeContem(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isEmpty()) {
                return null;
            }
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    private static Specification<UsuarioEntity> perfilAcessoContem(Enum<?> cargo) {
        return (root, query, cb) -> {
            if (cargo == null) {
                return null;
            }
            return cb.equal(root.get("perfilAcesso"), cargo);
        };
    }

    private static Specification<UsuarioEntity> ativoIgual(Boolean ativo) {
        return (root, query, cb) -> {
            if (ativo == null) {
                return null;
            }
            return cb.equal(root.get("ativo"), ativo);
        };
    }
}