package com.fiap.gastrolinkapi.controller;

import com.fiap.gastrolinkapi.dto.request.LoginRequest;
import com.fiap.gastrolinkapi.dto.response.LoginResponse;
import com.fiap.gastrolinkapi.exception.CredenciasInvalidasException;
import com.fiap.gastrolinkapi.infra.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints para autenticação de usuários via JWT")
public class AuthController {

    private AuthenticationManager authManager;
    private JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authManager, JwtUtil jwtUtil) {
        this.authManager = authManager;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Autenticar usuário", description = "Autentica um usuário usando login e senha, retornando um token JWT válido por um período determinado.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso, token JWT retornado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2FvX3NpbHZhIiwiaWF0IjoxNjk2MTI1NTAwLCJleHAiOjE2OTYxMjkxMDB9.signature"
                                    }"""))),
            @ApiResponse(responseCode = "400", description = "Validação falhou - campos obrigatórios vazios",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "type": "about:blank",
                                      "title": "Erro de Validação",
                                      "status": 400,
                                      "detail": "Um ou mais campos estão inválidos ou ausentes.",
                                      "errors": [
                                        {"campo": "login", "mensagem": "Login é obrigatório"},
                                        {"campo": "senha", "mensagem": "Senha é obrigatória"}
                                      ]
                                    }"""))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas - login ou senha incorretos",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "type": "https://api.gastrolink.com/erros/credenciais-invalidas",
                                      "title": "Autenticação Falhou",
                                      "status": 401,
                                      "detail": "Login ou senha inválidos"
                                    }""")))
    })
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.login(), request.senha()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new CredenciasInvalidasException("Login ou senha inválidos");
        }

        String token = jwtUtil.gerarToken(request.login());
        return ResponseEntity.ok(new LoginResponse(token));
    }
}
