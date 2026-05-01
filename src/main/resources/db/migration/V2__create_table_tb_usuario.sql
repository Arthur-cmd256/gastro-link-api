create table tb_usuario (
    id                      bigint          not null    auto_increment,
    nome                    varchar(150)    not null,
    email                   varchar(150)    not null    unique,
    login                   varchar(50)     not null    unique,
    senha                   varchar(255)    not null,
    tipo_usuario            varchar(50)     not null,
    data_ultima_alteracao   timestamp       not null,
    endereco_id             bigint          not null,

    primary key (id)
);