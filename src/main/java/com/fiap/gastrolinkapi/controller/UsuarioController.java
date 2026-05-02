package com.fiap.gastrolinkapi.controller;

import com.fiap.gastrolinkapi.domain.service.UsuarioService;
import com.fiap.gastrolinkapi.dto.request.UsuarioCadastroRequest;
import com.fiap.gastrolinkapi.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gestão de usuários (Clientes e Donos de Restaurantes)")
public class UsuarioController {
    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Cadastrar novo usuário", description = "Cria um novo usuário no sistema com validação de e-mail e login únicos. O e-mail e login não podem estar já registrados em outro usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "id": 1,
                                      "nome": "João Silva",
                                      "email": "joao@example.com",
                                      "login": "joao_silva",
                                      "tipoUsuario": "CLIENTE",
                                      "dataUltimaAlteracao": "2023-10-01T10:00:00",
                                      "endereco": {
                                        "logradouro": "Rua das Flores",
                                        "numero": "123",
                                        "complemento": null,
                                        "bairro": "Centro",
                                        "cidade": "São Paulo",
                                        "uf": "SP",
                                        "cep": "01234-567"
                                      }
                                    }"""))),
            @ApiResponse(responseCode = "400", description = "Validação falhou - campos obrigatórios vazios, e-mail inválido, etc.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "type": "about:blank",
                                      "title": "Erro de Validação",
                                      "status": 400,
                                      "detail": "Um ou mais campos estão inválidos ou ausentes.",
                                      "errors": [
                                        {"campo": "email", "mensagem": "deve ser um endereço de e-mail bem formado"},
                                        {"campo": "nome", "mensagem": "Nome é obrigatório"}
                                      ]
                                    }"""))),
            @ApiResponse(responseCode = "409", description = "Conflito - E-mail ou Login já cadastrado no sistema",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "type": "https://api.gastrolink.com/erros/conflito-de-dados",
                                      "title": "Conflito de Cadastro",
                                      "status": 409,
                                      "detail": "Email já cadastrado"
                                    }""")))
    })
    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioResponse> cadastrarUsuario(@RequestBody @Valid UsuarioCadastroRequest dto, UriComponentsBuilder uriBuilder) {
        UsuarioResponse usuarioResponse = this.usuarioService.cadastrar(dto);
        var uri = uriBuilder.path("/usuarios/{id}").buildAndExpand(usuarioResponse.id()).toUri();
        return ResponseEntity.created(uri).body(usuarioResponse);
    }

    @GetMapping
    @Operation(summary = "Listar ou buscar usuários", description = "Lista todos os usuários ou busca por nome com paginação.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                {
                  "content": [
                    {
                      "id": 1,
                      "nome": "João Silva",
                      "email": "joao@example.com",
                      "login": "joao_silva",
                      "tipoUsuario": "CLIENTE",
                      "dataUltimaAlteracao": "2023-10-01T10:00:00",
                      "endereco": {
                        "logradouro": "Rua das Flores",
                        "numero": "123",
                        "complemento": null,
                        "bairro": "Centro",
                        "cidade": "São Paulo",
                        "uf": "SP",
                        "cep": "01234-567"
                      }
                    }
                  ],
                  "totalElements": 1,
                  "totalPages": 1,
                  "number": 0,
                  "size": 10,
                  "empty": false
                }""")))
    })
    public ResponseEntity<Page<UsuarioResponse>> buscarUsuarios(
            @ParameterObject @PageableDefault(size = 10, sort = "nome") Pageable pageable,
            @Parameter(description = "Nome do usuário para busca (case-insensitive, opcional)", example = "João")
            @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(this.usuarioService.buscar(pageable, nome));
    }
}
