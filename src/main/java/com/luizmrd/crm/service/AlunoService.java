package com.luizmrd.crm.service;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.repository.IAlunoRepository;
import com.luizmrd.crm.dto.AlunoFiltroRequest;
import com.luizmrd.crm.service.especificacao.AlunoEspecificacao;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlunoService {
    public final IAlunoRepository alunoRepository;

    public AlunoService(IAlunoRepository alunoRepository){
        this.alunoRepository = alunoRepository;
    }


    public List<AlunoEntity> buscarAlunosFiltro(AlunoFiltroRequest filtro){
        return alunoRepository.findAll(AlunoEspecificacao.comFiltro(filtro));
    }


}
