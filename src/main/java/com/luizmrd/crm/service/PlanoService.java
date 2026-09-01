package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoAcessoCurto;
import com.luizmrd.crm.database.model.PlanoEntity;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.database.repository.IPlanoRepository;
import com.luizmrd.crm.dto.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.AlunoRequestDto;
import com.luizmrd.crm.dto.PlanoRequestDto;
import com.luizmrd.crm.exception.BadRequestException;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import com.luizmrd.crm.service.especificacao.AlunoEspecificacao;
import org.springframework.stereotype.Service;

import java.util.List;

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


}
