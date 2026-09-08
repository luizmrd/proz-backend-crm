package com.luizmrd.crm.dto;

import com.luizmrd.crm.database.model.InscricaoEntity;
import com.luizmrd.crm.database.model.enuns.PresencaStatusEnum;

import java.time.LocalDateTime;

public record InscricaoRespostaDto(
        Long id,
        PresencaStatusEnum presencaStatus,
        LocalDateTime dataInscricao,
        AlunoResumoDto aluno
) {
    public static InscricaoRespostaDto de(InscricaoEntity inscricao) {
        if (inscricao == null) return null;
        return new InscricaoRespostaDto(
                inscricao.getId(),
                inscricao.getPresencaStatus(),
                inscricao.getDataInscricao().atStartOfDay(),
                AlunoResumoDto.de(inscricao.getAluno())
        );
    }
}