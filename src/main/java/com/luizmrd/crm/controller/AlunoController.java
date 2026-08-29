package com.luizmrd.crm.controller;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.dto.AlunoFiltroRequest;
import com.luizmrd.crm.service.AlunoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/alunos")
public class AlunoController {

    public final AlunoService alunoService;

    public AlunoController(AlunoService alunoService){
        this.alunoService =alunoService;
    }

    @GetMapping
    public List<AlunoEntity> bucarAlunosComFiltro(@RequestParam AlunoFiltroRequest filtro){
        return alunoService.buscarAlunosFiltro(filtro);
    }

}
