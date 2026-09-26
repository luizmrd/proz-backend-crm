package com.luizmrd.crm.dto.aluno;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.enuns.SexoEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;

public record AlunoCadastradoResponseDto(
        Long id,
        String nome,
        SexoEnum sexo,
        String codigoAcesso,
        StatusEnum statusEnum
) {
    public static AlunoCadastradoResponseDto de(AlunoEntity aluno) {
        return new AlunoCadastradoResponseDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getSexo(),
                aluno.getCodigoAcesso(),
                aluno.getStatusEnum()
        );
    }
}
