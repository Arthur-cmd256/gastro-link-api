package com.fiap.gastrolinkapi.domain.entity;

import com.fiap.gastrolinkapi.dto.request.UsuarioCadastroRequest;
import com.fiap.gastrolinkapi.domain.enums.TipoUsuario;
import jakarta.persistence.*;

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

    public Usuario(UsuarioCadastroRequest dados, LocalDateTime dataUltimaAlteracao) {
        this.nome = dados.nome();
        this.email = dados.email();
        this.login = dados.login();
        this.senha = dados.senha();
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

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public LocalDateTime getDataUltimaAlteracao() {
        return dataUltimaAlteracao;
    }

    public Endereco getEndereco() {
        return endereco;
    }
}
