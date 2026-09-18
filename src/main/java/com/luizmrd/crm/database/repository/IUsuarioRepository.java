package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Long>, JpaSpecificationExecutor<UsuarioEntity> {
    boolean existsByEmail(String email);

    boolean existsByCargo(PerfilAcessoEnum cargo);

    Optional<UsuarioEntity> findByEmail(String email);
}
