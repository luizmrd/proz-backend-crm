package com.luizmrd.crm.dto.aula;

import java.time.LocalDate;
import java.time.LocalTime;

public record AulaRequestDto(
        String modalidade,
        LocalDate data,
        LocalTime dataInicio,
        LocalTime dataFim,
        String professor,
        Integer limiteVagas,
        String sala
) {
}
