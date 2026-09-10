package com.luizmrd.crm.dto.contrato;

public record ContratoDto(

        Long aluno,
        Boolean aceiteContrato,
        String termosContrato,
        Integer diaVencimentoMensalidade,
        String assinaturaDigital
) {
}
