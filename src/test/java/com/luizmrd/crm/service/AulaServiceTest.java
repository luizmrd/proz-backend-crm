package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.database.model.InscricaoEntity;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IAulaRepository;
import com.luizmrd.crm.database.repository.IInscricaoRepository;
import com.luizmrd.crm.exception.BadRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AulaServiceTest {

    @Mock
    private IAulaRepository aulaRepository;

    @Mock
    private IAlunoRepository alunoRepository;

    @Mock
    private IInscricaoRepository inscricaoRepository;

    @InjectMocks
    private AulaService aulaService;

    @Test
    void inscreverAlunoNaAula_deveSalvarInscricaoQuandoValido() {
        AulaEntity aula = AulaEntity.builder()
                .id(1L)
                .data(LocalDate.now())
                .dataInicio(LocalTime.of(18, 0))
                .dataFim(LocalTime.of(19, 0))
                .limiteVagas(10)
                .inscricoes(new ArrayList<>())
                .build();

        AlunoEntity aluno = AlunoEntity.builder()
                .id(2L)
                .inscricoes(new ArrayList<>())
                .build();

        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
        when(alunoRepository.findById(2L)).thenReturn(Optional.of(aluno));

        aulaService.inscreverAlunoNaAula(1L, 2L);

        verify(inscricaoRepository).save(any(InscricaoEntity.class));
    }

    @Test
    void inscreverAlunoNaAula_deveLancarBadRequestQuandoLimiteVagasAtingido() {
        AlunoEntity outroAluno = AlunoEntity.builder().id(99L).build();
        InscricaoEntity inscricaoExistente = InscricaoEntity.builder().aluno(outroAluno).build();

        AulaEntity aula = AulaEntity.builder()
                .id(1L)
                .limiteVagas(1)
                .inscricoes(List.of(inscricaoExistente))
                .build();

        AlunoEntity aluno = AlunoEntity.builder()
                .id(2L)
                .inscricoes(new ArrayList<>())
                .build();

        when(aulaRepository.findById(1L)).thenReturn(Optional.of(aula));
        when(alunoRepository.findById(2L)).thenReturn(Optional.of(aluno));

        assertThrows(BadRequestException.class, () -> aulaService.inscreverAlunoNaAula(1L, 2L));
        verify(inscricaoRepository, never()).save(any());
    }

    @Test
    void inscreverAlunoNaAula_deveLancarBadRequestQuandoHouverConflitoDeHorario() {
        LocalDate dataAula = LocalDate.now();
        AulaEntity aulaExistente = AulaEntity.builder()
                .id(10L)
                .data(dataAula)
                .dataInicio(LocalTime.of(18, 0))
                .dataFim(LocalTime.of(19, 0))
                .build();

        AulaEntity novaAula = AulaEntity.builder()
                .id(11L)
                .data(dataAula)
                .dataInicio(LocalTime.of(18, 30))
                .dataFim(LocalTime.of(19, 30))
                .limiteVagas(10)
                .inscricoes(new ArrayList<>())
                .build();

        AlunoEntity aluno = AlunoEntity.builder()
                .id(2L)
                .inscricoes(List.of(InscricaoEntity.builder().aula(aulaExistente).build()))
                .build();

        when(aulaRepository.findById(11L)).thenReturn(Optional.of(novaAula));
        when(alunoRepository.findById(2L)).thenReturn(Optional.of(aluno));

        assertThrows(BadRequestException.class, () -> aulaService.inscreverAlunoNaAula(11L, 2L));
        verify(inscricaoRepository, never()).save(any());
    }
}
