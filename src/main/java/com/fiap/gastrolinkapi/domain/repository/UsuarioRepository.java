package com.fiap.gastrolinkapi.domain.repository;

import com.fiap.gastrolinkapi.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    boolean existsByEmail(String email);
    boolean existsByLogin(String login);
}
