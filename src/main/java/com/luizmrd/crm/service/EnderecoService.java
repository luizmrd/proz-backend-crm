package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.EnderecoEntity;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IEnderecoRepository;
import com.luizmrd.crm.dto.endereco.EnderecoAtualizarRequestDto;
import com.luizmrd.crm.dto.endereco.EnderecoRequestDto;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {


    private final IEnderecoRepository enderecoRepository;
    private final IAlunoRepository alunoRepository;

    public EnderecoService(IEnderecoRepository enderecoRepository, IAlunoRepository alunoRepository){
        this.enderecoRepository = enderecoRepository;
        this.alunoRepository = alunoRepository;
    }

    @Transactional
    public void criarEndereco(EnderecoRequestDto endereco) {

        AlunoEntity aluno = alunoRepository.findById(endereco.alunoId())
                .orElseThrow(() -> new ResourceNotFoundException("Aluno não encontrado"));

        EnderecoEntity enderecoEntity = enderecoRepository.save(
                EnderecoEntity.builder()
                        .aluno(aluno)
                        .rua(endereco.rua())
                        .cidade(endereco.cidade())
                        .cep(endereco.cep())
                        .numero(endereco.numero())
                        .complemento(endereco.complemento())
                        .uf(endereco.uf())
                        .build()
        );

        aluno.setEndereco(enderecoEntity);
    }

    public EnderecoEntity buscarEndereco(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado"));
    }

    @Transactional
    public EnderecoEntity atualizarEndereco(Long id, EnderecoAtualizarRequestDto endereco) {
        EnderecoEntity enderecoEntity = buscarEndereco(id);

        if (endereco.rua() != null) {
            enderecoEntity.setRua(endereco.rua());
        }
        if (endereco.cidade() != null) {
            enderecoEntity.setCidade(endereco.cidade());
        }
        if (endereco.cep() != null) {
            enderecoEntity.setCep(endereco.cep());
        }
        if (endereco.numero() != null) {
            enderecoEntity.setNumero(endereco.numero());
        }
        if (endereco.complemento() != null) {
            enderecoEntity.setComplemento(endereco.complemento());
        }
        if (endereco.uf() != null) {
            enderecoEntity.setUf(endereco.uf());
        }

        return enderecoRepository.save(enderecoEntity);
    }

}
