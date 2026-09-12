package com.luizmrd.crm.service;

import com.luizmrd.crm.config.CodigoHash;
import com.luizmrd.crm.database.model.UsuarioEntity;
import com.luizmrd.crm.database.model.enuns.PerfilAcessoEnum;
import com.luizmrd.crm.database.repository.IUsuarioRepository;
import com.luizmrd.crm.dto.usuario.UsuarioAtualizarDto;
import com.luizmrd.crm.dto.usuario.UsuarioDto;
import com.luizmrd.crm.exception.BadRequestException;
import com.luizmrd.crm.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    private final IUsuarioRepository usuarioRepository;
    private final CodigoHash codigoHash;


    public UsuarioService(IUsuarioRepository usuarioRepository, CodigoHash codigoHash) {
        this.codigoHash = codigoHash;
        this.usuarioRepository = usuarioRepository;
    }


    public UsuarioEntity buscarUsuarioPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
    }



    public void criarUsuario(UsuarioDto usuarioDto){
        if (usuarioRepository.existsByEmail(usuarioDto.email())) {
            throw new BadRequestException("Já existe um usuário com este e-mail");
        }
        if (usuarioDto.cargo() == PerfilAcessoEnum.ADMINISTRADOR) {
            throw new ResourceNotFoundException("Não é permitido criar outro usuário ADMIN");
        }

        String senhaCriptografada = codigoHash.gerarHash(usuarioDto.senha());


        usuarioRepository.save(UsuarioEntity.builder()
                .nome(usuarioDto.nome())
                .email(usuarioDto.email())
                .senha(senhaCriptografada)
                .cargo(usuarioDto.cargo())
                .telefone(usuarioDto.telefone())
                        .ativo(true)
                .build());
    }

    public void atualizarUsuario(Long id, UsuarioAtualizarDto usuarioD) {
        UsuarioEntity usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        usuario.setNome(usuarioD.nome());
        usuario.setEmail(usuarioD.email());
        usuario.setTelefone(usuarioD.telefone());

        usuarioRepository.save(usuario);


    }
}
