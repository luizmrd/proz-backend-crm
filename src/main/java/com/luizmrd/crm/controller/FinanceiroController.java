package com.luizmrd.crm.controller;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.dto.financeiro.RecebimentoQuitadoResponseDto;
import com.luizmrd.crm.dto.financeiro.RecebimentoQuitarRequestDto;
import com.luizmrd.crm.dto.financeiro.RecebimentoRequestDto;
import com.luizmrd.crm.service.FinanceiroService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/financeiro/recebimento")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarRecebimento(@RequestBody RecebimentoRequestDto recebimento) {
        financeiroService.criarRecebimento(recebimento);
    }


    @PostMapping("/quitar/{id}")
    public ResponseEntity<RecebimentoQuitadoResponseDto> quitarRecebimento (@PathVariable Long id, @RequestBody RecebimentoQuitarRequestDto recebimento) throws BadRequestException {
        RecebimentoQuitadoResponseDto response =financeiroService.quitarRecebimento(id,recebimento);
       return ResponseEntity.ok(response);
    }
}
