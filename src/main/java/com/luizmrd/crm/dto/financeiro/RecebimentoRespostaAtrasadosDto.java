package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RecebimentoRespostaAtrasadosDto(
        Long id,
        Long alunoId,
        String alunoNome,
        BigDecimal valor,
        LocalDate dataVencimento,
        StatusPagamentoEnum status
) {
    public static RecebimentoRespostaAtrasadosDto de(RecebimentoEntity recebimento){
        if(recebimento == null) return null;
        return new RecebimentoRespostaAtrasadosDto(
                recebimento.getId(),
                recebimento.getAluno() != null ? recebimento.getAluno().getId() : null,
                recebimento.getAluno() != null ? recebimento.getAluno().getNome() : null,
                recebimento.getValor(),
                recebimento.getDataVencimento(),
                recebimento.getStatusPagamento()
        );
    }
}
