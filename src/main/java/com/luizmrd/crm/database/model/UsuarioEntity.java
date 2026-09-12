package com.luizmrd.crm.database.model;

import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;
import jakarta.persistence.*;
import lombok.*;

@Table(name = "usuario")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(nullable = false)
    private String senha;
    @Column(nullable = false)
    private PerfilAcessoEnum cargo;
    @Column(nullable = false)
    private String telefone;

}
