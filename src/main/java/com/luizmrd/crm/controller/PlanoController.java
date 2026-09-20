package com.luizmrd.crm.controller;

import com.luizmrd.crm.commom.ApiResponse;
import com.luizmrd.crm.commom.PageResponse;
import com.luizmrd.crm.dto.plano.PlanoAtualizarRequestDto;
import com.luizmrd.crm.dto.plano.PlanoRequestDto;
import com.luizmrd.crm.dto.plano.PlanoResponseDto;
import com.luizmrd.crm.service.PlanoService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/plano")
public class PlanoController {

    public final PlanoService planoService;

    public PlanoController(PlanoService planoService){
        this.planoService = planoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarPlano(@RequestBody PlanoRequestDto plano){
        planoService.criarPlano(plano);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<PlanoResponseDto>>> listarPlanos(
            @PageableDefault(size = 15) Pageable pageable){
        PageResponse<PlanoResponseDto> planos = PageResponse.de(
                planoService.listarPlanos(pageable), PlanoResponseDto::de);
        return ResponseEntity.ok(ApiResponse.of(planos));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PlanoResponseDto>> buscarPlano(@PathVariable Long id){
        PlanoResponseDto plano = PlanoResponseDto.de(planoService.buscarPlano(id));
        return ResponseEntity.ok(ApiResponse.of(plano));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarPlano(@PathVariable Long id,
                               @RequestBody PlanoAtualizarRequestDto planoAtualizarRequestDto){
        planoService.atualizarPlano(id, planoAtualizarRequestDto);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarPlanoParcial(@PathVariable Long id,
                                      @RequestBody PlanoAtualizarRequestDto planoAtualizarRequestDto){
        planoService.atualizarPlano(id, planoAtualizarRequestDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluirPlano(@PathVariable Long id){
        planoService.excluirPlano(id);
    }

}
