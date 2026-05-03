package com.fiap.gastrolinkapi.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "O campo login é obrigatório")
        String login,
        @NotBlank(message = "O campo senha é obrigatório")
        String senha) {
}
