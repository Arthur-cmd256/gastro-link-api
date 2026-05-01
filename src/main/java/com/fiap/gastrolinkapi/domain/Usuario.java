package com.fiap.gastrolinkapi.domain;

import jakarta.persistence.*;

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
    @Column(nullable = false)
    private String tipoUsuario;
    @Column(nullable = false)
    private String dataUltimaAlteracao;

    @Embedded
    private Endereco endereco;
}
