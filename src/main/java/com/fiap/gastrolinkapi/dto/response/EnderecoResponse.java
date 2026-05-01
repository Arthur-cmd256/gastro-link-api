package com.fiap.gastrolinkapi.dto.response;

import com.fiap.gastrolinkapi.domain.entity.Endereco;

public record EnderecoResponse(
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep
) {
    public EnderecoResponse(Endereco endereco) {
        this(endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getUf(),
                endereco.getCep());
    }
}
