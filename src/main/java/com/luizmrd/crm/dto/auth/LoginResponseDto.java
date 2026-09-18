package com.luizmrd.crm.dto.auth;

public record LoginResponseDto(
        String token,
        String tipo,
        long expiracaoSegundos,
        UsuarioAutenticadoDto usuario
) {
}
