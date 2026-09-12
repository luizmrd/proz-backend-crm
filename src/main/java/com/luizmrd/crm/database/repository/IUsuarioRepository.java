package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    boolean existsByEmail(String email);


}
