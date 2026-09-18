package com.luizmrd.crm.dto.auth;

public record LoginRequestDto(
        String email,
        String senha
) {
}
