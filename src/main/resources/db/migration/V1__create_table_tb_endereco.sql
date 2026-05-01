CREATE TABLE tb_endereco (
     id             BIGINT          PRIMARY KEY,
     logradouro     VARCHAR(255)    NOT NULL,
     numero         VARCHAR(20)     NOT NULL,
     complemento    VARCHAR(100),
     bairro         VARCHAR(100)    NOT NULL,
     cidade         VARCHAR(100)    NOT NULL,
     uf             VARCHAR(2)      NOT NULL,
     cep            VARCHAR(10)     NOT NULL
);