package com.luizmrd.crm.dto;


import com.luizmrd.crm.database.model.AlunoEntity;

public record AlunoResumoDto(
        Long id,
        String nome
) {
    public static AlunoResumoDto de(AlunoEntity aluno) {
        if (aluno == null) return null;
        return new AlunoResumoDto(aluno.getId(), aluno.getNome());
    }
}