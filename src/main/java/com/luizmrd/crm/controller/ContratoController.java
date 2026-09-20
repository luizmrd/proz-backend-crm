package com.luizmrd.crm.controller;

import com.luizmrd.crm.commom.ApiResponse;
import com.luizmrd.crm.commom.PageResponse;
import com.luizmrd.crm.dto.contrato.ContratoAtualizarRequestDto;
import com.luizmrd.crm.dto.contrato.ContratoDto;
import com.luizmrd.crm.dto.contrato.ContratoResponseDto;
import com.luizmrd.crm.service.ContratoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ContratoResponseDto>>> listarContratos(
            @PageableDefault(size = 15) Pageable pageable){
        PageResponse<ContratoResponseDto> contratos = PageResponse.de(
                contratoService.listarContratos(pageable), ContratoResponseDto::de);
        return ResponseEntity.ok(ApiResponse.of(contratos));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<ApiResponse<ContratoResponseDto>> buscarContratoPorAluno(@PathVariable Long alunoId){
        ContratoResponseDto contrato = ContratoResponseDto.de(contratoService.buscarContratoPorAluno(alunoId));
        return ResponseEntity.ok(ApiResponse.of(contrato));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContratoResponseDto>> buscarContrato(@PathVariable Long id){
        ContratoResponseDto contrato = ContratoResponseDto.de(contratoService.buscarContrato(id));
        return ResponseEntity.ok(ApiResponse.of(contrato));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarContrato(@PathVariable Long id,
                                  @RequestBody ContratoAtualizarRequestDto contratoAtualizarRequestDto){
        contratoService.atualizarContrato(id, contratoAtualizarRequestDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarContratoParcial(@PathVariable Long id,
                                         @RequestBody ContratoAtualizarRequestDto contratoAtualizarRequestDto){
        contratoService.atualizarContrato(id, contratoAtualizarRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirContrato(@PathVariable Long id){
        contratoService.excluirContrato(id);
    }

}
