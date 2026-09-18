package com.luizmrd.crm.service;

import com.luizmrd.crm.config.JwtService;
import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.dto.auth.LoginRequestDto;
import com.luizmrd.crm.dto.auth.LoginResponseDto;
import com.luizmrd.crm.dto.auth.UsuarioAutenticadoDto;
import com.luizmrd.crm.exception.UnauthorizedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public LoginResponseDto login(LoginRequestDto request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.senha())
            );

            UsuarioEntity usuario = (UsuarioEntity) authentication.getPrincipal();
            String token = jwtService.gerarToken(usuario);

            return new LoginResponseDto(
                    token,
                    "Bearer",
                    jwtService.getExpiracaoSegundos(),
                    UsuarioAutenticadoDto.de(usuario)
            );
        } catch (AuthenticationException ex) {
            throw new UnauthorizedException("USUARIO_OU_SENHA_INVALIDOS", "E-mail ou senha inválidos");
        }
    }
}
