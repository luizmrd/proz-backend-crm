package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoAcessoCurto;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.enuns.StatusEnum;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.dto.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.AlunoRequestDto;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import com.luizmrd.crm.service.especificacao.AlunoEspecificacao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {
    public final IAlunoRepository alunoRepository;

    public final CodigoAcessoCurto codigoAcessoCurto;

    public AlunoService(IAlunoRepository alunoRepository,CodigoAcessoCurto codigoAcessoCurto){
        this.alunoRepository = alunoRepository;
        this.codigoAcessoCurto = codigoAcessoCurto;
    }


    public List<AlunoEntity> buscarAlunosFiltro(AlunoFiltroRequestDto filtro){
        return alunoRepository.findAll(AlunoEspecificacao.comFiltro(filtro));
    }

    public void criarAluno(AlunoRequestDto alunoRequestDto){
       if(alunoRepository.existsByCpf(alunoRequestDto.cpf())){
           throw  new ResourceNotFoundException("Cpf já em uso!");
       }
       if(alunoRepository.existsByEmail(alunoRequestDto.email())){
           throw  new ResourceNotFoundException("Email já em uso!");
       }

       String cod;
       do {
           cod = codigoAcessoCurto.gerar(8);

           System.out.println("Código gerado: " + cod);
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
                       .plano(alunoRequestDto.plano())
                       .valorMensal(alunoRequestDto.valorMensal())
                       .statusPagamento(alunoRequestDto.statusPagamento())
                .build()
        );

    }
}
