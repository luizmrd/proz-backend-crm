package com.luizmrd.crm.controller;

import com.luizmrd.crm.commom.ApiResponse;
import com.luizmrd.crm.database.model.AlunoEntity;
import com.luizmrd.crm.dto.aluno.AlunoPerfilAtualizarRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoRiscoEvasaoResponseDto;
import com.luizmrd.crm.dto.aluno.AlunoResumoResponseDto;
import com.luizmrd.crm.service.AlunoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/alunos")
public class AlunoController {

    public final AlunoService alunoService;

    public AlunoController(AlunoService alunoService){
        this.alunoService =alunoService;
    }

    @GetMapping
    public ApiResponse<List<AlunoResumoResponseDto>> bucarAlunosComFiltro(@ModelAttribute AlunoFiltroRequestDto filtro){
        List<AlunoResumoResponseDto> alunos = alunoService.buscarAlunosFiltro(filtro).stream()
                .map(AlunoResumoResponseDto::de)
                .toList();
        return ApiResponse.of(alunos);
    }

    @GetMapping("/risco-evasao")
    public ResponseEntity<ApiResponse<List<AlunoRiscoEvasaoResponseDto>>> listarAlunosRiscoEvasao(){
        List<AlunoRiscoEvasaoResponseDto> alunos = alunoService.listarAlunosRiscoEvasao();
        return ResponseEntity.ok(ApiResponse.of(alunos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AlunoResumoResponseDto>> bucarAlunosId(@PathVariable Long id){
        AlunoEntity aluno = alunoService.buscarAlunoId(id);
        AlunoResumoResponseDto dto = AlunoResumoResponseDto.de(aluno);
        return ResponseEntity.ok(ApiResponse.of(dto));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAluno(@RequestBody AlunoRequestDto aluno){
        alunoService.criarAluno(aluno);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.UPGRADE_REQUIRED)
    public void atualizarPerfilAluno(@PathVariable Long id,
                                     @RequestBody AlunoPerfilAtualizarRequestDto alunoPerfilAtualizarRequestDto){
        alunoService.atualizaAluno(id, alunoPerfilAtualizarRequestDto);
    }

}
