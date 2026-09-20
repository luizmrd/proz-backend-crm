package com.luizmrd.crm.dto.contrato;

import com.luizmrd.crm.database.model.ContratoEntity;

import java.time.LocalDateTime;

public record ContratoResponseDto(
        Long id,
        Long alunoId,
        String alunoNome,
        Boolean aceiteContrato,
        String termosContrato,
        Integer diaVencimentoMensalidade,
        String assinaturaDigital,
        LocalDateTime aceitoEm
) {

    public static ContratoResponseDto de(ContratoEntity contrato) {
        return new ContratoResponseDto(
                contrato.getId(),
                contrato.getAluno() != null ? contrato.getAluno().getId() : null,
                contrato.getAluno() != null ? contrato.getAluno().getNome() : null,
                contrato.getAceiteContrato(),
                contrato.getTermosContrato(),
                contrato.getDiaVencimentoMensalidade(),
                contrato.getAssinaturaDigital(),
                contrato.getAceitoEm()
        );
    }
}
