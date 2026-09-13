package com.luizmrd.crm.controller;

import com.luizmrd.crm.dto.recebimento.RecebimentoRequestDto;
import com.luizmrd.crm.service.RecebimentoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/recebimento")
public class RecebimentoController {

    private final RecebimentoService recebimentoService;

    public RecebimentoController(RecebimentoService recebimentoService) {
        this.recebimentoService = recebimentoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarRecebimento(@RequestBody RecebimentoRequestDto recebimento) {
        recebimentoService.criarRecebimento(recebimento);
    }

}
