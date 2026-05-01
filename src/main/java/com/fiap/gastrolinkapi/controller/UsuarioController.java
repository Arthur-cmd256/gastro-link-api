package com.fiap.gastrolinkapi.controller;

import com.fiap.gastrolinkapi.domain.service.UsuarioService;
import com.fiap.gastrolinkapi.dto.request.UsuarioCadastroRequest;
import com.fiap.gastrolinkapi.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gestão de usuários (Clientes e Donos de Restaurantes)")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Cadastrar novo usuário", description = "Cria um usuário no sistema com validação de e-mail e login únicos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de validação inválidos (campos obrigatórios vazios, e-mail inválido, etc.)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "type": "about:blank",
                                      "title": "Erro de Validação",
                                      "status": 400,
                                      "detail": "Um ou mais campos estão inválidos ou ausentes.",
                                      "errors": [
                                        {"campo": "email", "mensagem": "debe ser un correo electrónico bien formado"},
                                        {"campo": "nome", "mensagem": "Nome é obrigatório"}
                                      ]
                                    }"""))),
            @ApiResponse(responseCode = "409", description = "E-mail ou Login já cadastrado no sistema",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "type": "https://api.seusistema.com/erros/conflito-de-dados",
                                      "title": "Conflito de Cadastro",
                                      "status": 409,
                                      "detail": "Email ja cadastrado"
                                    }""")))
    })
    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody @Valid UsuarioCadastroRequest dto, UriComponentsBuilder uriBuilder) {
        UsuarioResponse usuarioResponse = this.usuarioService.cadastrar(dto);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioResponse.id()).toUri();
        return ResponseEntity.created(uri).body(usuarioResponse);
    }
}
