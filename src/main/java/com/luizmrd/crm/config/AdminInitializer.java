package com.luizmrd.crm.config;

import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;
import com.luizmrd.crm.database.repository.IUsuarioRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminInitializer.class);

    private final IUsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final String nome;
    private final String email;
    private final String senha;

    public AdminInitializer(IUsuarioRepository usuarioRepository,
                            PasswordEncoder passwordEncoder,
                            @Value("${app.admin.nome}") String nome,
                            @Value("${app.admin.email}") String email,
                            @Value("${app.admin.senha}") String senha) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (usuarioRepository.existsByCargo(PerfilAcessoEnum.ADMINISTRADOR)) {
            return;
        }

        usuarioRepository.save(UsuarioEntity.builder()
                .nome(nome)
                .email(email)
                .senha(passwordEncoder.encode(senha))
                .cargo(PerfilAcessoEnum.ADMINISTRADOR)
                .telefone("")
                .ativo(true)
                .build());

        log.info("Usuário administrador inicial criado com o e-mail {}", email);
    }
}
