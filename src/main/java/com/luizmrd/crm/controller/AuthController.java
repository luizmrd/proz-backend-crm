package com.luizmrd.crm.controller;

import com.luizmrd.crm.commom.ApiResponse;
import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.dto.auth.LoginRequestDto;
import com.luizmrd.crm.dto.auth.LoginResponseDto;
import com.luizmrd.crm.dto.auth.UsuarioAutenticadoDto;
import com.luizmrd.crm.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<LoginResponseDto> login(@RequestBody LoginRequestDto request) {
        return ApiResponse.of(authService.login(request));
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<UsuarioAutenticadoDto> me(Authentication authentication) {
        UsuarioEntity usuario = (UsuarioEntity) authentication.getPrincipal();
        return ApiResponse.of(UsuarioAutenticadoDto.de(usuario));
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout() {
    }
}
