package com.luizmrd.crm.controller;

import com.luizmrd.crm.dto.endereco.EnderecoAtualizarRequestDto;
import com.luizmrd.crm.dto.endereco.EnderecoRequestDto;
import com.luizmrd.crm.service.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/enderecos")
public class EnderecoController {

    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarEndereco(@RequestBody EnderecoRequestDto endereco) {
        enderecoService.criarEndereco(endereco);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarEndereco(@PathVariable Long id,
                                  @RequestBody EnderecoAtualizarRequestDto endereco) {
        enderecoService.atualizarEndereco(id, endereco);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarEnderecoParcial(@PathVariable Long id,
                                         @RequestBody EnderecoAtualizarRequestDto endereco) {
        enderecoService.atualizarEndereco(id, endereco);
    }

}
