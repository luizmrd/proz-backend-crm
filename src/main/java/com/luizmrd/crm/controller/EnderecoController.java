package com.luizmrd.crm.controller;

import com.luizmrd.crm.commom.ApiResponse;
import com.luizmrd.crm.dto.endereco.EnderecoAtualizarRequestDto;
import com.luizmrd.crm.dto.endereco.EnderecoRequestDto;
import com.luizmrd.crm.dto.endereco.EnderecoResponseDto;
import com.luizmrd.crm.service.EnderecoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EnderecoResponseDto>> buscarEndereco(@PathVariable Long id) {
        EnderecoResponseDto endereco = EnderecoResponseDto.de(enderecoService.buscarEndereco(id));
        return ResponseEntity.ok(ApiResponse.of(endereco));
    }

    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<ApiResponse<EnderecoResponseDto>> buscarEnderecoPorAluno(@PathVariable Long alunoId) {
        EnderecoResponseDto endereco = EnderecoResponseDto.de(enderecoService.buscarEnderecoPorAluno(alunoId));
        return ResponseEntity.ok(ApiResponse.of(endereco));
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
