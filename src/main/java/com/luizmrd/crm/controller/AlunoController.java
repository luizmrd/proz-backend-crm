package com.luizmrd.crm.controller;

import com.luizmrd.crm.database.model.AlunoEntity;

import com.luizmrd.crm.dto.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.AlunoRequestDto;
import com.luizmrd.crm.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/alunos")
public class AlunoController {

    public final AlunoService alunoService;

    public AlunoController(AlunoService alunoService){
        this.alunoService =alunoService;
    }

    @GetMapping
    public List<AlunoEntity> bucarAlunosComFiltro(@RequestParam AlunoFiltroRequestDto filtro){
        return alunoService.buscarAlunosFiltro(filtro);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAluno(@RequestBody AlunoRequestDto aluno){
        alunoService.criarAluno(aluno);
    }

}
