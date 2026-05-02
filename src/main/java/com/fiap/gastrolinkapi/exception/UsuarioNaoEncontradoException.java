package com.fiap.gastrolinkapi.exception;

public class UsuarioNaoEncontradoException extends RuntimeException{
    public UsuarioNaoEncontradoException(String mensage){
        super(mensage);
    }
}
