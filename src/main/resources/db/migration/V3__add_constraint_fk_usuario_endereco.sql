ALTER TABLE tb_usuario
    ADD CONSTRAINT fk_usuario_endereco
        FOREIGN KEY (endereco_id) REFERENCES tb_endereco(id);