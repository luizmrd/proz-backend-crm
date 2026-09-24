package com.luizmrd.crm.dto.endereco;

import com.luizmrd.crm.database.model.EnderecoEntity;

public record EnderecoResponseDto(
        Long id,
        Long alunoId,
        String rua,
        String cidade,
        String cep,
        Integer numero,
        String complemento,
        String uf
) {

    public static EnderecoResponseDto de(EnderecoEntity endereco) {
        return new EnderecoResponseDto(
                endereco.getId(),
                endereco.getAluno() != null ? endereco.getAluno().getId() : null,
                endereco.getRua(),
                endereco.getCidade(),
                endereco.getCep(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getUf()
        );
    }
}
