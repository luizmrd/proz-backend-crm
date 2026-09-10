package com.luizmrd.crm.dto.aula;

import java.time.LocalDate;
import java.time.LocalTime;

public record AulaFiltroRequesDto(
        LocalDate data,
        LocalTime dataInicio,
        LocalTime dataFim,
        String status,
        String professor,
        String modalidade
) {
}
