package com.luizmrd.crm.controller;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.dto.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.AlunoPerfilAtualizarRequestDto;
import com.luizmrd.crm.dto.AlunoRequestDto;
import com.luizmrd.crm.dto.AulaRequestDto;
import com.luizmrd.crm.service.AlunoService;
import com.luizmrd.crm.service.AulaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/aulas")
public class AulaController {

    public final AulaService aulaService;

    public AulaController(AulaService aulaService){
        this.aulaService = aulaService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAluno(@RequestBody AulaRequestDto aula){
        aulaService.criarAula(aula);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public AulaEntity bucarAulaPorId(@RequestParam Long id){
        return aulaService.bucarAulaPorId(id);
    }
    @PatchMapping
    public void atualizarAula(@RequestParam Long id, @RequestBody AulaRequestDto aula){
        aulaService.atualizarAula(id, aula);
    }


}
