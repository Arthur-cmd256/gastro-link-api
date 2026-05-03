package com.fiap.gastrolinkapi.controller;

import com.fiap.gastrolinkapi.domain.service.UsuarioService;
import com.fiap.gastrolinkapi.dto.request.AtualizaSenhaRequest;
import com.fiap.gastrolinkapi.dto.request.AtualizaUsuarioRequest;
import com.fiap.gastrolinkapi.dto.request.UsuarioCadastroRequest;
import com.fiap.gastrolinkapi.dto.response.UsuarioResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@RestController
@RequestMapping("/v1/usuarios")
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

    @GetMapping("/buscar")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Listar ou buscar usuários", description = "Lista todos os usuários ou busca por nome com paginação. **Requer autenticação com token JWT.**")
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
                }"""))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - token JWT ausente ou inválido")
    })
    public ResponseEntity<Page<UsuarioResponse>> buscarUsuarios(
            @Parameter(hidden = true) Pageable pageable,
            @Parameter(description = "Nome do usuário para busca (case-insensitive, opcional)", example = "João")
            @RequestParam(required = false) String nome) {
        return ResponseEntity.ok(this.usuarioService.buscar(pageable, nome));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário específico. **Requer autenticação com token JWT.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "id": 1,
                                      "nome": "João Silva Atualizado",
                                      "email": "joao@example.com",
                                      "login": "joao_silva",
                                      "tipoUsuario": "CLIENTE",
                                      "dataUltimaAlteracao": "2023-10-02T10:00:00",
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
            @ApiResponse(responseCode = "401", description = "Não autenticado - token JWT ausente ou inválido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<UsuarioResponse> atualizarUsuario(@PathVariable Long id, @RequestBody @Valid AtualizaUsuarioRequest dto) {
        UsuarioResponse usuarioResponse = this.usuarioService.atualizar(id, dto);
        return ResponseEntity.ok(usuarioResponse);
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário específico do sistema. **Requer autenticação com token JWT.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Não autenticado - token JWT ausente ou inválido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        this.usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/senha/{id}")
    @SecurityRequirement(name = "Bearer Authentication")
    @Operation(summary = "Atualizar senha do usuário", description = "Atualiza a senha de um usuário específico. **Requer autenticação com token JWT.**")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Validação falhou - senhas inválidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = """
                                    {
                                      "type": "about:blank",
                                      "title": "Erro de Validação",
                                      "status": 400,
                                      "detail": "Um ou mais campos estão inválidos ou ausentes.",
                                      "errors": [
                                        {"campo": "senhaAnterior", "mensagem": "Senha anterior é obrigatória"},
                                        {"campo": "novaSenha", "mensagem": "Nova senha é obrigatória"}
                                      ]
                                    }"""))),
            @ApiResponse(responseCode = "401", description = "Não autenticado - token JWT ausente ou inválido"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    public ResponseEntity<Void> atualizarSenha(@PathVariable Long id, @RequestBody @Valid AtualizaSenhaRequest dto) {
        usuarioService.atualizarSenha(id, dto);
        return ResponseEntity.noContent().build();
    }
}
