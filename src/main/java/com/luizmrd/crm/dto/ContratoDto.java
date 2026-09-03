package com.luizmrd.crm.dto;

import com.luizmrd.crm.database.model.AlunoEntity;

public record ContratoDto(

        Long aluno,
        Boolean aceiteContrato,
        String termosContrato,
        Integer diaVencimentoMensalidade,
        String assinaturaDigital
) {
}
