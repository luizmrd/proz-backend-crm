package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoAcessoCurto;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.PlanoEntity;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IPlanoRepository;
import com.luizmrd.crm.dto.aluno.AlunoPerfilAtualizarRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoResumoResponseDto;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import com.luizmrd.crm.service.especificacao.AlunoEspecificacao;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {
    public final IAlunoRepository alunoRepository;

    public final CodigoAcessoCurto codigoAcessoCurto;
    public final IPlanoRepository planoRepository;

    public AlunoService(IAlunoRepository alunoRepository,CodigoAcessoCurto codigoAcessoCurto, IPlanoRepository planoRepository){
        this.alunoRepository = alunoRepository;
        this.codigoAcessoCurto = codigoAcessoCurto;
        this.planoRepository = planoRepository;
    }


    public List<AlunoEntity> buscarAlunosFiltro(AlunoFiltroRequestDto filtro){
        return alunoRepository.findAll(AlunoEspecificacao.comFiltro(filtro));
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
        aluno.setDataNascimento(alunoPerfilAtualizarRequestDto.dataNacimento());
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
                .dataNascimento(alunoRequestDto.dataNacimento())
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
