package com.luizmrd.crm.controller;

import com.luizmrd.crm.dto.usuario.UsuarioAtualizarDto;
import com.luizmrd.crm.dto.usuario.UsuarioAtualizarPermissoes;
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

    @PutMapping("/atualizar-perfil/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioAtualizarDto usuarioDto){
        usuarioService.atualizarUsuario(id, usuarioDto);
    }
    @PatchMapping("/atualizar-permissoes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarPermissoesUsuario(@PathVariable Long id, @RequestBody UsuarioAtualizarPermissoes usuario){
        usuarioService.atualizarPerfilAcessoUsuario(id, usuario);
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UsuarioResumoDto buscarUsuarioPorId(@PathVariable Long id) {
        return UsuarioResumoDto.de(usuarioService.buscarUsuarioPorId(id));
    }
    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativarUsuario(@PathVariable Long id) {
        usuarioService.desativarUsuario(id);
    }

    @PatchMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarUsuario(@PathVariable Long id) {
        usuarioService.ativarUsuario(id);

    }
}
