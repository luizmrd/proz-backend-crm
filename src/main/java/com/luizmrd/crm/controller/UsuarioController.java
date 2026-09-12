package com.luizmrd.crm.controller;

import com.luizmrd.crm.dto.usuario.UsuarioDto;
import com.luizmrd.crm.dto.usuario.UsuarioResumoDto;
import com.luizmrd.crm.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;


    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void criarUsuario(@RequestBody UsuarioDto usuarioDto){
        usuarioService.criarUsuario(usuarioDto);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResumoDto buscarUsuarioPorId(@PathVariable Long id) {
        return UsuarioResumoDto.de(usuarioService.buscarUsuarioPorId(id));
    }

}
