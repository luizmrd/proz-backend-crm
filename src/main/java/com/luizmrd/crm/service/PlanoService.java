package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.PlanoEntity;
import com.luizmrd.crm.database.repository.IPlanoRepository;
import com.luizmrd.crm.dto.plano.PlanoAtualizarRequestDto;
import com.luizmrd.crm.dto.plano.PlanoRequestDto;
import com.luizmrd.crm.exception.BadRequestException;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PlanoService {


    private final IPlanoRepository planoRepository;

    public PlanoService(IPlanoRepository planoRepository){
        this.planoRepository = planoRepository;
    }

   public void criarPlano(PlanoRequestDto planoRequestDto){

       if (planoRepository.findByNome(planoRequestDto.nome()).isPresent()) {
           throw new BadRequestException("Plano já criado");
       }

       planoRepository.save(
               PlanoEntity.builder()
                       .nome(planoRequestDto.nome())
                       .descricao(planoRequestDto.descricao())
                       .valorPadrao(planoRequestDto.valorPadrao())
                       .build()

       );

   }

    public Page<PlanoEntity> listarPlanos(Pageable pageable) {
        return planoRepository.findAll(pageable);
    }

    public PlanoEntity buscarPlano(Long id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plano não encontrado"));
    }

    @Transactional
    public PlanoEntity atualizarPlano(Long id, PlanoAtualizarRequestDto planoAtualizarRequestDto) {
        PlanoEntity plano = buscarPlano(id);

        if (planoAtualizarRequestDto.nome() != null) {
            planoRepository.findByNome(planoAtualizarRequestDto.nome())
                    .filter(p -> !p.getId().equals(id))
                    .ifPresent(p -> {
                        throw new BadRequestException("Já existe um plano com este nome");
                    });
            plano.setNome(planoAtualizarRequestDto.nome());
        }
        if (planoAtualizarRequestDto.descricao() != null) {
            plano.setDescricao(planoAtualizarRequestDto.descricao());
        }
        if (planoAtualizarRequestDto.valorPadrao() != null) {
            plano.setValorPadrao(planoAtualizarRequestDto.valorPadrao());
        }
        if (planoAtualizarRequestDto.ativo() != null) {
            plano.setAtivo(planoAtualizarRequestDto.ativo());
        }

        return planoRepository.save(plano);
    }

    @Transactional
    public void excluirPlano(Long id) {
        PlanoEntity plano = buscarPlano(id);
        planoRepository.delete(plano);
    }


}
