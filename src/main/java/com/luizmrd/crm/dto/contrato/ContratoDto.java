package com.luizmrd.crm.dto.contrato;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ContratoDto(
        @NotNull(message = "O ID do aluno é obrigatório")
        Long aluno,
        @NotNull(message = "O aceite do contrato é obrigatório")
        Boolean aceiteContrato,
        @NotBlank(message = "Os termos do contrato são obrigatórios")
        String termosContrato,
        @NotNull(message = "O dia de vencimento é obrigatório")
        @Min(value = 1, message = "O dia de vencimento deve ser entre 1 e 31")
        @Max(value = 31, message = "O dia de vencimento deve ser entre 1 e 31")
        Integer diaVencimentoMensalidade,
        @NotBlank(message = "A assinatura digital é obrigatória")
        String assinaturaDigital
) {
}
