package com.luizmrd.crm.controller;

import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.dto.financeiro.*;
import com.luizmrd.crm.dto.inscricao.FinanceiroResumoDto;
import com.luizmrd.crm.service.FinanceiroService;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/financeiro")
public class FinanceiroController {

    private final FinanceiroService financeiroService;

    public FinanceiroController(FinanceiroService financeiroService) {
        this.financeiroService = financeiroService;
    }

    @PostMapping("/recebimento")
    @ResponseStatus(HttpStatus.CREATED)
    public void criarRecebimento(@RequestBody RecebimentoRequestDto recebimento) {
        financeiroService.criarRecebimento(recebimento);
    }


    @PostMapping("/recebimento/quitar/{id}")
    public ResponseEntity<RecebimentoQuitadoResponseDto> quitarRecebimento (@PathVariable Long id, @RequestBody RecebimentoQuitarRequestDto recebimento) throws BadRequestException {
        RecebimentoQuitadoResponseDto response =financeiroService.quitarRecebimento(id,recebimento);
       return ResponseEntity.ok(response);
    }

    @PostMapping("/contas-pagar")
    @ResponseStatus(HttpStatus.CREATED)
    public void criarContasPagar(@RequestBody ContasPagarRequestDto contas){
        financeiroService.criarContasPagar(contas);
    }

    @PostMapping("/contas-pagar/quitar/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void quitarContasPagar(@PathVariable Long id){
        financeiroService.quitarContasPagar(id);
    }

    @GetMapping("/resumo")
    public ResponseEntity<FinanceiroResumoDto> obterResumo() {
        FinanceiroResumoDto resumo = financeiroService.obterResumoFluxoCaixaParaTeste();
        return ResponseEntity.ok((resumo));
    }

    @GetMapping("/recebimentos")
    public ResponseEntity<List<RecebimentoRespostaDto>> listarRecebimentos(
            @ModelAttribute RecebimentoFiltroDto filtro
    ) {
        List<RecebimentoRespostaDto> recebimentos = financeiroService.listarRecebimentos(filtro);
        return ResponseEntity.ok((recebimentos));
    }
    @GetMapping("/recebimentos/atrasados")
    public ResponseEntity<List<RecebimentoRespostaAtrasadosDto>> listarRecebimentosAtrasados() {

        List<RecebimentoRespostaAtrasadosDto> recebimentos = financeiroService.listarRecebimentosAtrasados();
        return ResponseEntity.ok((recebimentos));
    }
    @GetMapping("/contas-pagar")
    public ResponseEntity<List<ContasPagarRespostaDto>> listarContasAtrasados() {

        List<ContasPagarRespostaDto> contas = financeiroService.listarContasPagar();
        return ResponseEntity.ok((contas));
    }



}
