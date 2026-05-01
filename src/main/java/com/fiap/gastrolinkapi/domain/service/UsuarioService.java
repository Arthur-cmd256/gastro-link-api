package com.fiap.gastrolinkapi.domain.service;

import com.fiap.gastrolinkapi.domain.entity.Usuario;
import com.fiap.gastrolinkapi.domain.repository.UsuarioRepository;
import com.fiap.gastrolinkapi.dto.request.UsuarioCadastroRequest;
import com.fiap.gastrolinkapi.dto.response.UsuarioResponse;
import com.fiap.gastrolinkapi.exception.EmailJaCadastradoException;
import com.fiap.gastrolinkapi.exception.LoginJaCadastradoException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;
    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public UsuarioResponse cadastrar(UsuarioCadastroRequest dto){
        validarDuplicidade(dto.email(), dto.login());
        Usuario usuario = new Usuario(dto, LocalDateTime.now());
        repository.save(usuario);
        return new UsuarioResponse(usuario);
    }

    public void validarDuplicidade(String email, String login) {
        if (repository.existsByEmail(email)) {
            throw new EmailJaCadastradoException();
        }
        if (repository.existsByLogin(login)) {
            throw new LoginJaCadastradoException();
        }
    }
}
