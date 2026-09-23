package com.luizmrd.crm.dto.endereco;

public record EnderecoAtualizarRequestDto(
        String rua,
        String cidade,
        Integer numero,
        String cep,
        String uf,
        String complemento
) {
}
