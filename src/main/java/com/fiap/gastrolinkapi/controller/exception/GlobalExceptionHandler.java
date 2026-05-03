package com.fiap.gastrolinkapi.controller.exception;

import com.fiap.gastrolinkapi.exception.CredenciasInvalidasException;
import com.fiap.gastrolinkapi.exception.JaCadastradoException;
import com.fiap.gastrolinkapi.exception.UsuarioNaoEncontradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JaCadastradoException.class)
    public ProblemDetail handleEmailJaCadastradoException(JaCadastradoException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                ex.getMessage()
        );
        problem.setTitle("Conflito de Cadastro");
        problem.setType(URI.create("https://api.seusistema.com/erros/conflito-de-dados"));
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Um ou mais campos estão inválidos ou ausentes."
        );

        problemDetail.setTitle("Erro de Validação");

        List<Map<String, String>> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fe -> {
                    assert fe.getDefaultMessage() != null;
                    return Map.of("campo", fe.getField(), "mensagem", fe.getDefaultMessage());
                })
                .toList();

        problemDetail.setProperty("errors", errors);

        return problemDetail;
    }

    @ExceptionHandler(UsuarioNaoEncontradoException.class)
    public ProblemDetail handleUsuarioNaoEncontradoException(UsuarioNaoEncontradoException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                ex.getMessage()
        );
        problemDetail.setTitle("Usuário Não Encontrado");
        problemDetail.setType(URI.create("https://api.seusistema.com/erros/usuario-nao-encontrado"));
        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleException(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocorreu um erro inesperado. Por favor, tente novamente mais tarde."
        );
        problemDetail.setTitle("Erro Interno do Servidor");
        problemDetail.setType(URI.create("https://api.seusistema.com/erros/erro-interno"));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);

    }

    @ExceptionHandler(CredenciasInvalidasException.class)
    public ResponseEntity<ProblemDetail> handleCredenciasInvalidasException(CredenciasInvalidasException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED,
                ex.getMessage()
        );
        problemDetail.setTitle("Autenticação Falhou");
        problemDetail.setType(URI.create("https://api.seusistema.com/erros/credenciais-invalidas"));
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }
}
