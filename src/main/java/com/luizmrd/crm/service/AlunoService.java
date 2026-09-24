package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoAcessoCurto;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.PlanoEntity;
import com.luizmrd.crm.database.model.enuns.MotivoRiscoEvasaoEnum;
import com.luizmrd.crm.database.model.enuns.PresencaStatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.model.enuns.StatusPagamentoEnum;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IInscricaoRepository;
import com.luizmrd.crm.database.repository.IPlanoRepository;
import com.luizmrd.crm.dto.aluno.AlunoPerfilAtualizarRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoRiscoEvasaoResponseDto;
import com.luizmrd.crm.dto.aluno.AlunoResumoResponseDto;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import com.luizmrd.crm.service.especificacao.AlunoEspecificacao;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AlunoService {

    private static final long DIAS_SEM_TREINO_RISCO = 15;

    public final IAlunoRepository alunoRepository;

    public final CodigoAcessoCurto codigoAcessoCurto;
    public final IPlanoRepository planoRepository;
    public final IInscricaoRepository inscricaoRepository;

    public AlunoService(IAlunoRepository alunoRepository,CodigoAcessoCurto codigoAcessoCurto, IPlanoRepository planoRepository,
                        IInscricaoRepository inscricaoRepository){
        this.alunoRepository = alunoRepository;
        this.codigoAcessoCurto = codigoAcessoCurto;
        this.planoRepository = planoRepository;
        this.inscricaoRepository = inscricaoRepository;
    }


    public List<AlunoEntity> buscarAlunosFiltro(AlunoFiltroRequestDto filtro){
        List<AlunoEntity> alunos = alunoRepository.findAll(AlunoEspecificacao.comFiltro(filtro));
        if (filtro.emRiscoEvasao() == null) {
            return alunos;
        }
        boolean desejado = filtro.emRiscoEvasao();
        return alunos.stream()
                .filter(aluno -> desejado == !motivosRiscoEvasao(aluno).isEmpty())
                .toList();
    }

    @Transactional
    public List<AlunoRiscoEvasaoResponseDto> listarAlunosRiscoEvasao(){
        LocalDate hoje = LocalDate.now();

        return alunoRepository.findByStatusEnum(StatusEnum.ATIVO).stream()
                .map(aluno -> {
                    List<MotivoRiscoEvasaoEnum> motivos = motivosRiscoEvasao(aluno);
                    if (motivos.isEmpty()) {
                        return null;
                    }
                    LocalDate ultimoTreino = buscarUltimoTreino(aluno.getId());
                    Long diasSemTreino = ultimoTreino == null
                            ? null
                            : ChronoUnit.DAYS.between(ultimoTreino, hoje);
                    return AlunoRiscoEvasaoResponseDto.de(aluno, ultimoTreino, diasSemTreino, motivos);
                })
                .filter(java.util.Objects::nonNull)
                .sorted(Comparator.comparing(AlunoRiscoEvasaoResponseDto::diasSemTreino,
                        Comparator.nullsFirst(Comparator.reverseOrder())))
                .toList();
    }

    private List<MotivoRiscoEvasaoEnum> motivosRiscoEvasao(AlunoEntity aluno){
        List<MotivoRiscoEvasaoEnum> motivos = new ArrayList<>();

        if (StatusPagamentoEnum.ATRASADO.equals(aluno.getStatusPagamento())) {
            motivos.add(MotivoRiscoEvasaoEnum.MENSALIDADE_ATRASADA);
        }

        if (estaSemFrequencia(aluno.getId())) {
            motivos.add(MotivoRiscoEvasaoEnum.SEM_FREQUENCIA);
        }

        return motivos;
    }

    private boolean estaSemFrequencia(Long alunoId){
        if (inscricaoRepository.countByAlunoId(alunoId) == 0) {
            return false;
        }
        LocalDate ultimoTreino = buscarUltimoTreino(alunoId);
        LocalDate limite = LocalDate.now().minusDays(DIAS_SEM_TREINO_RISCO);
        return ultimoTreino == null || ultimoTreino.isBefore(limite);
    }

    private LocalDate buscarUltimoTreino(Long alunoId){
        return inscricaoRepository.buscarUltimaPresenca(alunoId, PresencaStatusEnum.PRESENTE);
    }

    public AlunoEntity buscarAlunoId(Long id){
       return alunoRepository.findById(id)
               .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));
    }

    @Transactional
    public  AlunoEntity atualizaAluno(Long id, AlunoPerfilAtualizarRequestDto alunoPerfilAtualizarRequestDto){
        AlunoEntity aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        aluno.setNome(alunoPerfilAtualizarRequestDto.nome());
        aluno.setCpf(alunoPerfilAtualizarRequestDto.cpf());
        aluno.setDataNascimento(alunoPerfilAtualizarRequestDto.dataNascimento());
        aluno.setSexo(alunoPerfilAtualizarRequestDto.sexo());
        aluno.setTelefone(alunoPerfilAtualizarRequestDto.telefone());
        aluno.setEmail(alunoPerfilAtualizarRequestDto.email());

        return alunoRepository.save(aluno);

    }


    public void criarAluno(AlunoRequestDto alunoRequestDto){
       if(alunoRepository.existsByCpf(alunoRequestDto.cpf())){
           throw  new ResourceNotFoundException("Cpf já em uso!");
       }
       if(alunoRepository.existsByEmail(alunoRequestDto.email())){
           throw  new ResourceNotFoundException("Email já em uso!");
       }
       PlanoEntity plano = planoRepository.findById(alunoRequestDto.plano())
               .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado")
               );

       String cod;
       do {
           cod = codigoAcessoCurto.gerar(8);

           System.out.println("Já existe? " +
                   alunoRepository.existsByCodigoAcesso(cod));
       }while (alunoRepository.existsByCodigoAcesso(cod));

       String codigoGerado = cod;

       alunoRepository.save(
               AlunoEntity.builder()
                .nome(alunoRequestDto.nome())
                .cpf(alunoRequestDto.cpf())
                .dataNascimento(alunoRequestDto.dataNascimento())
                .sexo(alunoRequestDto.sexo())
                .telefone(alunoRequestDto.telefone())
                .email(alunoRequestDto.email())
                .codigoAcesso(cod)
                       .plano(plano)
                       .valorMensal(plano.getValorPadrao())
                       .statusPagamento(alunoRequestDto.statusPagamento())
                .build()
        );

    }
}
