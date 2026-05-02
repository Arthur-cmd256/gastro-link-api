package com.fiap.gastrolinkapi.domain.service;

import com.fiap.gastrolinkapi.domain.entity.Usuario;
import com.fiap.gastrolinkapi.domain.repository.UsuarioRepository;
import com.fiap.gastrolinkapi.dto.request.UsuarioCadastroRequest;
import com.fiap.gastrolinkapi.dto.response.UsuarioResponse;
import com.fiap.gastrolinkapi.exception.EmailJaCadastradoException;
import com.fiap.gastrolinkapi.exception.LoginJaCadastradoException;
import com.fiap.gastrolinkapi.exception.UsuarioNaoEncontradoException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public Page<UsuarioResponse> buscar(Pageable pageable, String usuarioNome) {
        if (usuarioNome == null || usuarioNome.isBlank()) {
            return repository.findAll(pageable).map(UsuarioResponse::new);
        }
        return repository.findByNomeContainingIgnoreCase(usuarioNome.trim(), pageable).map(UsuarioResponse::new);
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
