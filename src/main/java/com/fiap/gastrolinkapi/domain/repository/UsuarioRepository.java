package com.fiap.gastrolinkapi.domain.repository;

import com.fiap.gastrolinkapi.domain.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);

    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    Optional<Usuario> findByLogin(String login);
}
