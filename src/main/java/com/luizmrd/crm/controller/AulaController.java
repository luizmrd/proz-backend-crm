package com.luizmrd.crm.controller;

import com.luizmrd.crm.commom.ApiResponse;
import com.luizmrd.crm.database.model.AulaEntity;
import com.luizmrd.crm.dto.aula.AulaFiltroRequesDto;
import com.luizmrd.crm.dto.aula.AulaRequestDto;
import com.luizmrd.crm.dto.aula.AulaRespostaDto;
import com.luizmrd.crm.service.AulaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/aulas")
public class AulaController {

    public final AulaService aulaService;

    public AulaController(AulaService aulaService){
        this.aulaService = aulaService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarAluno(@RequestBody AulaRequestDto aula){
        aulaService.criarAula(aula);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ApiResponse<AulaRespostaDto>> buscarPorId(@PathVariable Long id) {
        AulaEntity aula = aulaService.bucarAulaPorId(id);
        AulaRespostaDto dto = AulaRespostaDto.de(aula);

        return ResponseEntity.ok(ApiResponse.of(dto));
    }


    @GetMapping
    public ApiResponse<List<AulaRespostaDto>> buscarAulasFiltro(@ModelAttribute AulaFiltroRequesDto filtro) {

        List<AulaEntity> aulas = aulaService.buscarAulasFiltro(filtro);

        List<AulaRespostaDto> dtos = aulas.stream()
                .map(AulaRespostaDto::de)
                .toList();

        return ApiResponse.of(dtos);
    }

    @PatchMapping
    public void atualizarAula(@RequestParam Long id, @RequestBody AulaRequestDto aula){
        aulaService.atualizarAula(id, aula);
    }

    @PostMapping("/{id}/cancelar")
    public void cancelarAula(@PathVariable Long id){
        aulaService.cancelarAula(id);
    }


    @PostMapping("/inscrever")
    @ResponseStatus(HttpStatus.OK)
    public void inscreverAluno(@RequestParam Long aulaId, @RequestParam Long alunoId) {
        aulaService.inscreverAlunoNaAula(aulaId, alunoId);
    }

    @DeleteMapping("/cancelar-inscricao")
    @ResponseStatus(HttpStatus.OK)
    public void cancelarInscricao(@RequestParam Long aulaId, @RequestParam Long alunoId) {
        aulaService.removerInscricao(aulaId, alunoId);
    }
    @PostMapping("lancar-presenca/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void lancarPresenca(@PathVariable Long id){
        aulaService.lancarPresenca(id);
    }

}
