package com.luizmrd.crm.dto.aula;

import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.database.model.enuns.StatusAula;
import com.luizmrd.crm.dto.inscricao.InscricaoRespostaDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AulaRespostaDto(
        Long id,
        String modalidade,
        LocalDate data,
        LocalTime horarioInicio,
        LocalTime horarioFim,
        String professor,
        Integer limiteVagas,
        String sala,
        StatusAula status,
        List<InscricaoRespostaDto> inscricoes
) {
    public static AulaRespostaDto de(AulaEntity aula) {
        if (aula == null) return null;

        List<InscricaoRespostaDto> inscricoesDto = aula.getInscricoes() != null ?
                aula.getInscricoes().stream()
                        .map(InscricaoRespostaDto::de)
                        .toList() : List.of();

        return new AulaRespostaDto(
                aula.getId(),
                aula.getModalidade(),
                aula.getData(),
                aula.getDataInicio(),
                aula.getDataFim(),
                aula.getProfessor(),
                aula.getLimiteVagas(),
                aula.getSala(),
                aula.getStatusAula(),
                inscricoesDto
        );
    }
}