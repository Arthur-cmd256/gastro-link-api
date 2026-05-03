package com.fiap.gastrolinkapi.domain.service;

import com.fiap.gastrolinkapi.domain.entity.Usuario;
import com.fiap.gastrolinkapi.domain.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public UserDetailsServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Usuario usuario = this.usuarioRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado"));

        return User.builder()
                .username(usuario.getLogin())
                .password(usuario.getSenha())
                .roles(usuario.getTipoUsuario().name())
                .build();
    }
}
