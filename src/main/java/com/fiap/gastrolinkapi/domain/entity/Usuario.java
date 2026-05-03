package com.fiap.gastrolinkapi.domain.entity;

import com.fiap.gastrolinkapi.dto.request.AtualizaSenhaRequest;
import com.fiap.gastrolinkapi.dto.request.AtualizaUsuarioRequest;
import com.fiap.gastrolinkapi.dto.request.UsuarioCadastroRequest;
import com.fiap.gastrolinkapi.domain.enums.TipoUsuario;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Table(name = "tb_usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false, unique = true)
    private String login;
    @Column(nullable = false)
    private String senha;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoUsuario tipoUsuario;
    @Column(nullable = false)
    private LocalDateTime dataUltimaAlteracao;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "endereco_id", nullable = false)
    private Endereco endereco;

    public Usuario() {}

    public Usuario(UsuarioCadastroRequest dados, LocalDateTime dataUltimaAlteracao, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.login = dados.login();
        this.senha = bCryptPasswordEncoder.encode(dados.senha());
        this.tipoUsuario = dados.tipoUsuario();
        this.dataUltimaAlteracao = dataUltimaAlteracao;
        this.endereco = new Endereco(dados.endereco());
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public LocalDateTime getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void atualizarInformacoes(@Valid AtualizaUsuarioRequest dados){
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.email() != null) {
            this.email = dados.email();
        }
        if (dados.login() != null) {
            this.login = dados.login();
        }
        if (dados.tipoUsuario() != null) {
            this.tipoUsuario = dados.tipoUsuario();
        }
        if (dados.endereco() != null) {
            this.endereco.atualizarInformacoes(dados.endereco());
        }
        this.dataUltimaAlteracao = LocalDateTime.now();
    }

    public void atualizarSenha(@Valid AtualizaSenhaRequest dto, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.senha = bCryptPasswordEncoder.encode(dto.senha());
        this.dataUltimaAlteracao = LocalDateTime.now();
    }
}
