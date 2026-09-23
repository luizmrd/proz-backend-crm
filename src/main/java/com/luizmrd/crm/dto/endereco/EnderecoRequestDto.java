package com.luizmrd.crm.dto.endereco;

public record EnderecoRequestDto(
        Long alunoId,
        String rua,
        String cidade,
        Integer numero,
        String cep,
        String uf,
        String complemento
) {
}
