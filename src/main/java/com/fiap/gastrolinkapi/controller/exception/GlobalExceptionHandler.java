package com.fiap.gastrolinkapi.controller.exception;

import com.fiap.gastrolinkapi.exception.JaCadastradoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
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
}
