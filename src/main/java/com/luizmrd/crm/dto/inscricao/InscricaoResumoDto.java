package com.luizmrd.crm.dto.inscricao;

import com.luizmrd.crm.database.model.InscricaoEntity;
import com.luizmrd.crm.database.model.enuns.PresencaStatusEnum;
import com.luizmrd.crm.dto.aluno.AlunoResumoDto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record InscricaoResumoDto(
        Long id,
        PresencaStatusEnum presencaStatus,
        LocalDate dataInscricao,
        AlunoResumoDto aluno
) {
    public static InscricaoResumoDto de(InscricaoEntity inscricao) {
        if (inscricao == null) return null;

        return new InscricaoResumoDto(
                inscricao.getId(),
                inscricao.getPresencaStatus(),
                inscricao.getDataInscricao(),
                AlunoResumoDto.de(inscricao.getAluno())
        );
    }
}