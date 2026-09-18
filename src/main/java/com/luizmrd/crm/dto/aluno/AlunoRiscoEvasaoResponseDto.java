package com.luizmrd.crm.dto.aluno;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.enuns.MotivoRiscoEvasaoEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.time.LocalDate;
import java.util.List;

public record AlunoRiscoEvasaoResponseDto(
        Long id,
        String nome,
        String cpf,
        String telefone,
        String email,
        StatusPagamentoEnum statusPagamento,
        LocalDate ultimoTreino,
        Long diasSemTreino,
        List<MotivoRiscoEvasaoEnum> motivos
) {
    public static AlunoRiscoEvasaoResponseDto de(AlunoEntity aluno,
                                                 LocalDate ultimoTreino,
                                                 Long diasSemTreino,
                                                 List<MotivoRiscoEvasaoEnum> motivos) {
        if (aluno == null) return null;
        return new AlunoRiscoEvasaoResponseDto(
                aluno.getId(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getTelefone(),
                aluno.getEmail(),
                aluno.getStatusPagamento(),
                ultimoTreino,
                diasSemTreino,
                motivos
        );
    }
}
