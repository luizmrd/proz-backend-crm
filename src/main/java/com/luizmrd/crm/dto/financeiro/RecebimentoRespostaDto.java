package com.luizmrd.crm.dto.financeiro;

import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.database.model.enuns.MetodoPagamentoEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RecebimentoRespostaDto(
        Long id,
        Long alunoId,
        String alunoNome,
        BigDecimal valor,
        LocalDate dataVencimento,
        LocalDateTime dataPagamento,
        MetodoPagamentoEnum metodo,
        StatusPagamentoEnum status,
        String recibo
) {
    public static RecebimentoRespostaDto de(RecebimentoEntity recebimento) {
        if (recebimento == null) return null;

        return new RecebimentoRespostaDto(
                recebimento.getId(),
                recebimento.getAluno() != null ? recebimento.getAluno().getId() : null,
                recebimento.getAluno() != null ? recebimento.getAluno().getNome() : null,
                recebimento.getValor(),
                recebimento.getDataVencimento(),
                recebimento.getDataPagamento(),
                recebimento.getMetodoPagamento(),
                recebimento.getStatusPagamento(),
                recebimento.getRecibo()
        );
    }
}