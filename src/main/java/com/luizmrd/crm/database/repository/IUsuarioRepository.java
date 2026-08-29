package com.luizmrd.crm.database.repository;

import com.luizmrd.crm.database.model.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<UsuarioEntity, Long> {


}
