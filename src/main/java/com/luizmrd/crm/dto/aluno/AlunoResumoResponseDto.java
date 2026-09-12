package com.luizmrd.crm.dto.aluno;

import com.luizmrd.crm.database.model.AlunoEntity;

import com.luizmrd.crm.database.model.enuns.SexoEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import jakarta.persistence.*;

import java.time.LocalDate;

public record AlunoResumoResponseDto(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String email,
        SexoEnum sexo,
        String codigoAcesso,
        StatusEnum statusEnum

) {
    public static AlunoResumoResponseDto de(AlunoEntity aluno) {
        return new AlunoResumoResponseDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getDataNascimento(),
                aluno.getTelefone(),
                aluno.getEmail(),
                aluno.getSexo(),
                aluno.getCodigoAcesso(),
                aluno.getStatusEnum()
        );
    }

}
