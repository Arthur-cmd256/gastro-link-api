package com.fiap.gastrolinkapi.dto.request;

import com.fiap.gastrolinkapi.domain.enums.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizaUsuarioRequest(
        @Schema(description = "Nome completo do usuário", example = "João da Silva")
        @NotBlank(message = "Nome é obrigatório")
        String nome,
        @Schema(description = "Endereço de e-mail do usuário, deve ser único", example = "joao@example.com")
        @NotBlank(message = "Email é obrigatório")
        @Email
        String email,
        @Schema(description = "Nome de usuário para login", example = "joao_silva")
        @NotBlank(message = "Login é obrigatório")
        String login,
        @Schema(description = "Tipo de usuário (CLIENTE ou RESTAURANTE)", example = "CLIENTE")
        @NotNull(message = "Tipo de usuário é obrigatório")
        TipoUsuario tipoUsuario,
        @Schema(description = "Dados de endereço do usuário")
        @NotNull(message = "Endereço é obrigatório")
        @Valid
        EnderecoCadastroRequest endereco
) {
}
