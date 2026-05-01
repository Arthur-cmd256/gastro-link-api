package com.fiap.gastrolinkapi.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record EnderecoCadastroRequest(
        @Schema(description = "Nome da rua ou avenida", example = "Rua das Flores")
        @NotBlank(message = "Logradouro é obrigatório")
        String logradouro,
        @Schema(description = "Número do imóvel", example = "123")
        @NotBlank(message = "Numero é obrigatório")
        String numero,
        @Schema(description = "Complemento do endereço (opcional)", example = "Apartamento 42")
        String complemento,
        @Schema(description = "Nome do bairro", example = "Centro")
        @NotBlank(message = "bairro é obrigatório")
        String bairro,
        @Schema(description = "Nome da cidade", example = "São Paulo")
        @NotBlank(message = "cidade é obrigatório")
        String cidade,
        @Schema(description = "Sigla do estado (UF)", example = "SP")
        @NotBlank(message = "UF é obrigatório")
        String uf,
        @Schema(description = "Código de endereçamento postal (CEP)", example = "01234-567")
        @NotBlank(message = "CEP é obrigatório")
        String cep
) {
}
