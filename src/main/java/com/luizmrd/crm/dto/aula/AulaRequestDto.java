package com.luizmrd.crm.dto.aula;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;

public record AulaRequestDto(
        @NotBlank(message = "A modalidade é obrigatória")
        String modalidade,
        @NotNull(message = "A data da aula é obrigatória")
        LocalDate data,
        @NotNull(message = "O horário de início é obrigatório")
        LocalTime dataInicio,
        @NotNull(message = "O horário de término é obrigatório")
        LocalTime dataFim,
        @NotBlank(message = "O nome do professor é obrigatório")
        String professor,
        @NotNull(message = "O limite de vagas é obrigatório")
        @Positive(message = "O limite de vagas deve ser maior que zero")
        Integer limiteVagas,
        @NotBlank(message = "A sala é obrigatória")
        String sala
) {
}
