package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.database.model.enuns.MetodoPagamentoEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IContasPagarRepository;
import com.luizmrd.crm.database.repository.IContratoRepository;
import com.luizmrd.crm.database.repository.IRecebimentoRepository;
import com.luizmrd.crm.dto.financeiro.RecebimentoQuitadoResponseDto;
import com.luizmrd.crm.dto.financeiro.RecebimentoQuitarRequestDto;
import com.luizmrd.crm.dto.inscricao.FinanceiroResumoDto;
import com.luizmrd.crm.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FinanceiroServiceTest {

    @Mock
    private IRecebimentoRepository recebimentoRepository;

    @Mock
    private IAlunoRepository alunoRepository;

    @Mock
    private IContratoRepository contratoRepository;

    @Mock
    private IContasPagarRepository contasPagarRepository;

    @InjectMocks
    private FinanceiroService financeiroService;

    @Test
    void quitarRecebimento_deveMarcarComoPagoEGerarRecibo() {
        AlunoEntity aluno = AlunoEntity.builder()
                .id(1L)
                .nome("Maria")
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .build();

        RecebimentoEntity recebimento = RecebimentoEntity.builder()
                .id(5L)
                .aluno(aluno)
                .valor(new BigDecimal("180.00"))
                .dataVencimento(LocalDate.now())
                .statusPagamento(StatusPagamentoEnum.PENDENTE)
                .build();

        when(recebimentoRepository.findById(5L)).thenReturn(Optional.of(recebimento));
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));
        when(recebimentoRepository.save(any(RecebimentoEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        RecebimentoQuitarRequestDto request = new RecebimentoQuitarRequestDto(1L, MetodoPagamentoEnum.PIX);
        RecebimentoQuitadoResponseDto resposta = financeiroService.quitarRecebimento(5L, request);

        assertEquals(StatusPagamentoEnum.PAGO, resposta.status());
        assertEquals(MetodoPagamentoEnum.PIX, resposta.metodo());
        assertNotNull(resposta.recibo());
        assertEquals(StatusPagamentoEnum.PAGO, aluno.getStatusPagamento());
    }

    @Test
    void quitarRecebimento_deveLancarBadRequestQuandoJaPago() {
        AlunoEntity aluno = AlunoEntity.builder().id(1L).build();
        RecebimentoEntity recebimento = RecebimentoEntity.builder()
                .id(5L)
                .aluno(aluno)
                .statusPagamento(StatusPagamentoEnum.PAGO)
                .build();

        when(recebimentoRepository.findById(5L)).thenReturn(Optional.of(recebimento));
        when(alunoRepository.findById(1L)).thenReturn(Optional.of(aluno));

        RecebimentoQuitarRequestDto request = new RecebimentoQuitarRequestDto(1L, MetodoPagamentoEnum.PIX);

        assertThrows(BadRequestException.class, () -> financeiroService.quitarRecebimento(5L, request));
    }

    @Test
    void obterResumoFluxoCaixa_deveCalcularTotaisESaldoLiquido() {
        when(recebimentoRepository.somarPorStatusEIntervalo(eq(StatusPagamentoEnum.PAGO), any(), any()))
                .thenReturn(new BigDecimal("1000.00"));
        when(recebimentoRepository.somarPorStatusEIntervalo(eq(StatusPagamentoEnum.AGUARDANDO), any(), any()))
                .thenReturn(new BigDecimal("200.00"));
        when(recebimentoRepository.somarPorStatusEIntervalo(eq(StatusPagamentoEnum.ATRASADO), any(), any()))
                .thenReturn(new BigDecimal("150.00"));
        when(contasPagarRepository.somarPorStatusEIntervalo(eq(StatusPagamentoEnum.PAGO), any(), any()))
                .thenReturn(new BigDecimal("400.00"));

        FinanceiroResumoDto resumo = financeiroService.obterResumoFluxoCaixaParaTeste();

        assertEquals(new BigDecimal("1000.00"), resumo.totalRecebido());
        assertEquals(new BigDecimal("350.00"), resumo.totalPendente());
        assertEquals(new BigDecimal("400.00"), resumo.totalDespesas());
        assertEquals(new BigDecimal("600.00"), resumo.saldoLiquido());
    }
}
