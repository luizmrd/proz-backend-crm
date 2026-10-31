package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoAcessoCurto;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.PlanoEntity;
import com.luizmrd.crm.database.model.enuns.MotivoRiscoEvasaoEnum;
import com.luizmrd.crm.database.model.enuns.PresencaStatusEnum;
import com.luizmrd.crm.database.model.enuns.SexoEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IInscricaoRepository;
import com.luizmrd.crm.database.repository.IPlanoRepository;
import com.luizmrd.crm.database.repository.IRecebimentoRepository;
import com.luizmrd.crm.dto.aluno.AlunoRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoRiscoEvasaoResponseDto;
import com.luizmrd.crm.exception.BadRequestException;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlunoServiceTest {

    @Mock
    private IAlunoRepository alunoRepository;

    @Mock
    private CodigoAcessoCurto codigoAcessoCurto;

    @Mock
    private IPlanoRepository planoRepository;

    @Mock
    private IInscricaoRepository inscricaoRepository;

    @Mock
    private IRecebimentoRepository recebimentoRepository;

    @InjectMocks
    private AlunoService alunoService;

    @Test
    void criarAluno_deveSalvarAlunoComValorDoPlanoECodigoAcesso() {
        PlanoEntity plano = PlanoEntity.builder()
                .id(1L)
                .nome("Mensal Muay Thai")
                .valorPadrao(new BigDecimal("150.00"))
                .build();

        AlunoRequestDto request = new AlunoRequestDto(
                "João Silva",
                "12345678900",
                LocalDate.of(1998, 5, 10),
                SexoEnum.MASCULINO,
                "11999999999",
                "joao@email.com",
                1L,
                StatusPagamentoEnum.PAGO
        );

        when(alunoRepository.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(planoRepository.findById(1L)).thenReturn(Optional.of(plano));
        when(codigoAcessoCurto.gerar(8)).thenReturn("ABC12345");
        when(alunoRepository.existsByCodigoAcesso("ABC12345")).thenReturn(false);

        alunoService.criarAluno(request);

        ArgumentCaptor<AlunoEntity> captor = ArgumentCaptor.forClass(AlunoEntity.class);
        verify(alunoRepository).save(captor.capture());

        AlunoEntity salvo = captor.getValue();
        assertEquals("João Silva", salvo.getNome());
        assertEquals("ABC12345", salvo.getCodigoAcesso());
        assertEquals(new BigDecimal("150.00"), salvo.getValorMensal());
        assertEquals(plano, salvo.getPlano());
    }

    @Test
    void criarAluno_deveLancarBadRequestQuandoCpfJaEmUso() {
        AlunoRequestDto request = new AlunoRequestDto(
                "João Silva",
                "12345678900",
                LocalDate.of(1998, 5, 10),
                SexoEnum.MASCULINO,
                "11999999999",
                "joao@email.com",
                1L,
                StatusPagamentoEnum.PAGO
        );

        when(alunoRepository.existsByCpf("12345678900")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> alunoService.criarAluno(request));
        verify(alunoRepository, never()).save(any());
    }

    @Test
    void criarAluno_deveLancarResourceNotFoundQuandoPlanoInexistente() {
        AlunoRequestDto request = new AlunoRequestDto(
                "João Silva",
                "12345678900",
                LocalDate.of(1998, 5, 10),
                SexoEnum.MASCULINO,
                "11999999999",
                "joao@email.com",
                99L,
                StatusPagamentoEnum.PAGO
        );

        when(alunoRepository.existsByCpf("12345678900")).thenReturn(false);
        when(alunoRepository.existsByEmail("joao@email.com")).thenReturn(false);
        when(planoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> alunoService.criarAluno(request));
    }

    @Test
    void listarAlunosRiscoEvasao_deveDetectarMensalidadeAtrasadaESemFrequencia() {
        AlunoEntity alunoEmRisco = AlunoEntity.builder()
                .id(10L)
                .nome("Carlos")
                .statusEnum(StatusEnum.ATIVO)
                .statusPagamento(StatusPagamentoEnum.ATRASADO)
                .build();

        when(alunoRepository.findByStatusEnum(StatusEnum.ATIVO)).thenReturn(List.of(alunoEmRisco));
        when(inscricaoRepository.countByAlunoId(10L)).thenReturn(3L);
        when(inscricaoRepository.buscarUltimaPresenca(10L, PresencaStatusEnum.PRESENTE))
                .thenReturn(LocalDate.now().minusDays(20));

        List<AlunoRiscoEvasaoResponseDto> resultado = alunoService.listarAlunosRiscoEvasao();

        assertEquals(1, resultado.size());
        AlunoRiscoEvasaoResponseDto dto = resultado.getFirst();
        assertEquals(10L, dto.id());
        assertTrue(dto.motivos().contains(MotivoRiscoEvasaoEnum.MENSALIDADE_ATRASADA));
        assertTrue(dto.motivos().contains(MotivoRiscoEvasaoEnum.SEM_FREQUENCIA));
    }
}
