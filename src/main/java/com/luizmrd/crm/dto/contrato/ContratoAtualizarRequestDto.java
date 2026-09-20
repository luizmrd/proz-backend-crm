package com.luizmrd.crm.dto.contrato;

public record ContratoAtualizarRequestDto(
        Boolean aceiteContrato,
        String termosContrato,
        Integer diaVencimentoMensalidade,
        String assinaturaDigital
) {
}
