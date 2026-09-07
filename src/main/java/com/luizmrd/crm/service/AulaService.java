package com.luizmrd.crm.service;


import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.database.model.InscricaoEntity;
import com.luizmrd.crm.database.model.enuns.PresencaStatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusAula;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IAulaRepository;
import com.luizmrd.crm.database.repository.IInscricaoRepository;
import com.luizmrd.crm.dto.AulaRequestDto;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AulaService {

    private final IAulaRepository aulaRepository;
    private final IAlunoRepository alunoRepository;
    private final IInscricaoRepository inscricaoRepository;

    public AulaService(IAulaRepository aulaRepository, IAlunoRepository alunoRepository, IInscricaoRepository inscricaoRepository) {
        this.aulaRepository = aulaRepository;
        this.alunoRepository = alunoRepository;
        this.inscricaoRepository = inscricaoRepository;
    }

    public void criarAula(AulaRequestDto aula){
        aulaRepository.save(
                AulaEntity.builder()
                        .modalidade(aula.modalidade())
                        .data(aula.data())
                        .dataInicio(aula.dataInicio())
                        .dataFim(aula.dataFim())
                        .professor(aula.professor())
                        .limiteVagas(aula.limiteVagas())
                        .sala(aula.sala())
                        .build()
        );
    }

    public AulaEntity bucarAulaPorId(Long id) {
        return aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula não encontrada"));
    }

    public void atualizarAula(Long id, AulaRequestDto aula) {
        AulaEntity aulaEntity = bucarAulaPorId(id);
        aulaEntity.setModalidade(aula.modalidade());
        aulaEntity.setData(aula.data());
        aulaEntity.setDataInicio(aula.dataInicio());
        aulaEntity.setDataFim(aula.dataFim());
        aulaEntity.setProfessor(aula.professor());
        aulaEntity.setLimiteVagas(aula.limiteVagas());
        aulaEntity.setSala(aula.sala());
        aulaRepository.save(aulaEntity);
    }

    public void cancelarAula(Long id) {
        AulaEntity aulaEntity = aulaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada"));
        aulaEntity.setStatusAula(StatusAula.CANCELADA);
        aulaRepository.save(aulaEntity);
    }

    public void inscreverAlunoNaAula(Long aulaId, Long idAluno){
        AulaEntity aula = aulaRepository.findById(aulaId)
                .orElseThrow(() -> new ResourceNotFoundException("Aula não encontrada"));

        AlunoEntity aluno = alunoRepository.findById(idAluno)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        boolean jaInscrito = aula.getInscricoes().stream()
                .anyMatch(inscricao -> inscricao.getAluno().getId().equals(aluno.getId()));
        if (jaInscrito) {
            throw new ResourceNotFoundException("Aluno já inscrito na aula");
        }

        if (aula.getInscricoes().size() >= aula.getLimiteVagas()) {
            throw new ResourceNotFoundException("Aula já atingiu o limite de vagas");
        }


        boolean possuiConflitoDeHorario = aluno.getInscricoes().stream()
                .anyMatch(inscricao -> {
                    AulaEntity aulaInscrita = inscricao.getAula();
                    return aulaInscrita.getData().equals(aula.getData()) &&
                            ((aulaInscrita.getDataInicio().isBefore(aula.getDataFim()) && aulaInscrita.getDataFim().isAfter(aula.getDataInicio())));
                });
        if (possuiConflitoDeHorario) {
            throw new ResourceNotFoundException("Aluno já possui uma aula nesse horário");
    }
        InscricaoEntity inscricao = InscricaoEntity.builder()
                .aluno(aluno)
                .aula(aula)
                .dataInscricao(java.time.LocalDate.now())
                .presencaStatus(PresencaStatusEnum.AGUARDANDO)
                .build();
        inscricaoRepository.save(inscricao);

}

}