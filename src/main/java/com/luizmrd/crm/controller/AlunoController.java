package com.luizmrd.crm.controller;

import com.luizmrd.crm.database.model.AlunoEntity;

import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.dto.aluno.AlunoPerfilAtualizarRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoFiltroRequestDto;
import com.luizmrd.crm.dto.aluno.AlunoRequestDto;
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
    public List<AlunoEntity> bucarAlunosComFiltro(@RequestParam AlunoFiltroRequestDto filtro){
        return alunoService.buscarAlunosFiltro(filtro);
    }
    @GetMapping("/{id}")
    public ResponseEntity<AlunoResumoResponseDto> bucarAlunosId(@PathVariable Long id){
        AlunoEntity aluno = alunoService.buscarAlunoId(id);
        AlunoResumoResponseDto dto = AlunoResumoResponseDto.de(aluno);
        return ResponseEntity.ok(dto);
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
