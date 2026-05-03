package com.fiap.gastrolinkapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record AtualizaSenhaRequest(
        @NotBlank(message = "Senha é obrigatória")
        String senha
) {
}
