package com.luizmrd.crm.controller;

import com.luizmrd.crm.database.model.AlunoEntity;

import com.luizmrd.crm.dto.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.AlunoRequestDto;
import com.luizmrd.crm.dto.PlanoRequestDto;
import com.luizmrd.crm.service.AlunoService;
import com.luizmrd.crm.service.PlanoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/plano")
public class PlanoController {

    public final PlanoService planoService;

    public PlanoController(PlanoService planoService){
        this.planoService = planoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarPlano(@RequestBody PlanoRequestDto plano){
        planoService.criarPlano(plano);
    }

}
