package com.luizmrd.crm.controller;

import com.luizmrd.crm.dto.contatoemergencia.ContatoEmergenciaAtualizarRequestDto;
import com.luizmrd.crm.dto.contatoemergencia.ContatoEmergenciaRequestDto;
import com.luizmrd.crm.service.ContatoEmergenciaService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/contatos-emergencia")
public class ContatoEmergenciaController {

    private final ContatoEmergenciaService contatoEmergenciaService;

    public ContatoEmergenciaController(ContatoEmergenciaService contatoEmergenciaService) {
        this.contatoEmergenciaService = contatoEmergenciaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarContatoEmergencia(@RequestBody ContatoEmergenciaRequestDto contatoEmergencia) {
        contatoEmergenciaService.criarContatoEmergencia(contatoEmergencia);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarContatoEmergencia(@PathVariable Long id,
                                           @RequestBody ContatoEmergenciaAtualizarRequestDto contatoEmergencia) {
        contatoEmergenciaService.atualizarContatoEmergencia(id, contatoEmergencia);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarContatoEmergenciaParcial(@PathVariable Long id,
                                                  @RequestBody ContatoEmergenciaAtualizarRequestDto contatoEmergencia) {
        contatoEmergenciaService.atualizarContatoEmergencia(id, contatoEmergencia);
    }

}
