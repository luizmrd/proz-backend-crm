package com.luizmrd.crm.controller;

import com.luizmrd.crm.dto.ContratoDto;
import com.luizmrd.crm.service.ContratoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contratos")
public class ContratoController {

    private final ContratoService contratoService;

    public ContratoController(ContratoService contratoService) {
        this.contratoService = contratoService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarContrato(@RequestBody ContratoDto contratoDto) {
        contratoService.criarContrato(contratoDto);
    }


}
