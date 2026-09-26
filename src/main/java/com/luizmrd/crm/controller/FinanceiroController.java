package com.luizmrd.crm.controller;

import com.luizmrd.crm.commom.ApiResponse;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.database.model.RecebimentoEntity;
import com.luizmrd.crm.dto.financeiro.*;
import com.luizmrd.crm.dto.inscricao.FinanceiroResumoDto;
import com.luizmrd.crm.service.FinanceiroService;
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
    public ResponseEntity<ApiResponse<RecebimentoQuitadoResponseDto>> quitarRecebimento (@PathVariable Long id, @RequestBody RecebimentoQuitarRequestDto recebimento) throws BadRequestException {
        RecebimentoQuitadoResponseDto response =financeiroService.quitarRecebimento(id,recebimento);
       return ResponseEntity.ok(ApiResponse.of(response));
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
    public ResponseEntity<ApiResponse<FinanceiroResumoDto>> obterResumo() {
        FinanceiroResumoDto resumo = financeiroService.obterResumoFluxoCaixaParaTeste();
        return ResponseEntity.ok(ApiResponse.of(resumo));
    }

    @GetMapping("/recebimentos")
    public ResponseEntity<ApiResponse<List<RecebimentoRespostaDto>>> listarRecebimentos(
            @ModelAttribute RecebimentoFiltroDto filtro
    ) {
        List<RecebimentoRespostaDto> recebimentos = financeiroService.listarRecebimentos(filtro);
        return ResponseEntity.ok(ApiResponse.of(recebimentos));
    }
    @GetMapping("/recebimentos/atrasados")
    public ResponseEntity<ApiResponse<List<RecebimentoRespostaAtrasadosDto>>> listarRecebimentosAtrasados() {

        List<RecebimentoRespostaAtrasadosDto> recebimentos = financeiroService.listarRecebimentosAtrasados();
        return ResponseEntity.ok(ApiResponse.of(recebimentos));
    }
    @GetMapping("/contas-pagar")
    public ResponseEntity<ApiResponse<List<ContasPagarRespostaDto>>> listarContasAtrasados() {

        List<ContasPagarRespostaDto> contas = financeiroService.listarContasPagar();
        return ResponseEntity.ok(ApiResponse.of(contas));
    }

    @GetMapping("/recebimentos/aluno/{id}")
    public ResponseEntity<ApiResponse<List<RecebimentoRespostaDto>>> listarRecebimentoPorAluno(@PathVariable Long id){

        List<RecebimentoRespostaDto> recebimentos = financeiroService.listarRecebimentoPorAluno(id);

        return ResponseEntity.ok(ApiResponse.of(recebimentos));


    }

}
