package com.fiap.gastrolinkapi.dto.response;

import com.fiap.gastrolinkapi.domain.entity.Usuario;
import com.fiap.gastrolinkapi.domain.enums.TipoUsuario;

import java.time.LocalDateTime;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        String login,
        TipoUsuario tipoUsuario,
        LocalDateTime dataUltimaAlteracao,
        EnderecoResponse endereco
) {
    public UsuarioResponse(Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                usuario.getTipoUsuario(),
                usuario.getDataUltimaAlteracao(),
                new EnderecoResponse(usuario.getEndereco())
        );
    }
}
