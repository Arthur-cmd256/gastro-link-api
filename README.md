# Gastro Link API

Sistema de gestão de restaurantes desenvolvido em Spring Boot, permitindo que clientes escolham restaurantes e façam pedidos online, enquanto os donos de restaurantes gerenciam suas operações.

## 🚀 Tecnologias Utilizadas

- **Backend**: Spring Boot 4.0.6
- **Java**: 17
- **Banco de Dados**: MySQL 8.0
- **Autenticação**: Spring Security com JWT
- **Documentação**: OpenAPI/Swagger
- **Containerização**: Docker & Docker Compose
- **Build**: Maven
- **ORM**: Spring Data JPA
- **Validação**: Jakarta Validation

## 📋 Requisitos

- Docker e Docker Compose instalados
- Ou: Java 17, Maven 3.9+, MySQL 8.0+

## 🏃 Como Executar

### Opção 1: Com Docker Compose (Recomendado)

```bash
# Clone o repositório
git clone <seu-repositorio>
cd gastro-link-api

# Defina variáveis de ambiente (opcional)
export JWT_SECRET=your-secret-key
export JWT_EXPIRATION=86400000

# Execute o Docker Compose
docker-compose up -d

# Acesse a aplicação
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui/index.html
```

### Opção 2: Execução Local

```bash
# Com MySQL rodando localmente
mvn clean install
mvn spring-boot:run
```

## 📚 Documentação da API

A documentação completa está disponível em:
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- **OpenAPI JSON**: http://localhost:8080/v3/api-docs

## 🔐 Autenticação

A API utiliza **JWT (JSON Web Tokens)** para autenticação.

### Fluxo de Autenticação

1. **Cadastrar novo usuário** (sem autenticação):
   ```
   POST /v1/usuarios/cadastro
   ```

2. **Efetuar login**:
   ```
   POST /v1/auth/login
   ```
   Receba um token JWT

3. **Usar o token** em requisições autenticadas:
   ```
   Authorization: Bearer seu_token_aqui
   ```

## 📡 Endpoints Principais

### Autenticação
- `POST /v1/auth/login` - Autenticar usuário (público)

### Usuários
- `POST /v1/usuarios/cadastro` - Cadastrar novo usuário (público)
- `GET /v1/usuarios/buscar` - Buscar usuários por nome (autenticado)
- `PUT /v1/usuarios/{id}` - Atualizar informações do usuário (autenticado)
- `PATCH /v1/usuarios/{id}` - Atualizar senha (autenticado)
- `DELETE /v1/usuarios/{id}` - Deletar usuário (autenticado)

## 👥 Tipos de Usuário

- **CLIENTE**: Usuário que acessa o sistema como cliente
- **DONO_RESTAURANTE**: Proprietário de um restaurante

## 🗄️ Estrutura do Banco de Dados

### Tabelas Principais

#### `tb_endereco`
- `id` (INT, PRIMARY KEY)
- `logradouro` (VARCHAR)
- `numero` (VARCHAR)
- `complemento` (VARCHAR)
- `bairro` (VARCHAR)
- `cidade` (VARCHAR)
- `uf` (VARCHAR)
- `cep` (VARCHAR)

#### `tb_usuario`
- `id` (BIGINT, PRIMARY KEY)
- `nome` (VARCHAR(100))
- `email` (VARCHAR(150), UNIQUE)
- `login` (VARCHAR(100), UNIQUE)
- `senha` (VARCHAR(255))
- `tipo_usuario` (VARCHAR(50))
- `data_ultima_alteracao` (DATETIME)
- `endereco_id` (INT, FOREIGN KEY)

## 📦 Variáveis de Ambiente

```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/gastro_link
SPRING_DATASOURCE_USERNAME: myuser
SPRING_DATASOURCE_PASSWORD: secret
JWT_SECRET: your-secret-key-change-in-production
JWT_EXPIRATION: 86400000 # 24 horas em ms
```

## 🧪 Exemplos de Requisições

### Cadastro de Usuário

```json
POST /v1/usuarios/cadastro
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@example.com",
  "login": "joao_silva",
  "senha": "senha123",
  "tipoUsuario": "CLIENTE",
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "123",
    "complemento": "Apto 456",
    "bairro": "Centro",
    "cidade": "São Paulo",
    "uf": "SP",
    "cep": "01234-567"
  }
}
```

### Login

```json
POST /v1/auth/login
Content-Type: application/json

{
  "login": "joao_silva",
  "senha": "senha123"
}

Response:
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### Buscar Usuários

```
GET /v1/usuarios/buscar?nome=João&page=0&size=10&sort=nome
Authorization: Bearer seu_token_aqui
```

## 🐳 Docker Compose - Strutura

O arquivo `compose.yaml` define:

- **mysql**: Container MySQL 8.0 com banco `gastro_link`
- **app**: Container Spring Boot com a aplicação

Ambos conectados na mesma rede Docker.

## 🛠️ Arquitetura da Aplicação

```
src/main/java/com/fiap/gastrolinkapi/
├── controller/              # Controladores REST
│   ├── AuthController.java
│   └── UsuarioController.java
├── domain/                  # Domínio da aplicação
│   ├── entity/             # Entidades JPA
│   ├── enums/              # Enumerações
│   ├── repository/         # Repositórios JPA
│   └── service/            # Serviços de negócio
├── dto/                    # Data Transfer Objects
│   ├── request/
│   └── response/
├── exception/              # Exceções customizadas
├── infra/
│   └── security/           # Configurações de segurança JWT
└── GastroLinkApiApplication.java
```

## ✨ Funcionalidades Implementadas

- ✅ Cadastro, atualização e exclusão de usuários
- ✅ Busca de usuários por nome com paginação
- ✅ Sistema de autenticação com JWT
- ✅ Níveis de acesso (Público / Autenticado)
- ✅ Validação de email único
- ✅ Validação de login único
- ✅ Troca de senha em endpoint separado
- ✅ Registro de data de última alteração
- ✅ Tratamento de erros com ProblemDetail (RFC 7807)
- ✅ Documentação completa com Swagger/OpenAPI
- ✅ Versionamento de API (/v1/)
- ✅ Migrations com Flyway

## 🔧 Troubleshooting

### Porta 3306 já em uso
```bash
docker-compose down
# Ou mude a porta em compose.yaml
```

### Erro de conexão com banco
```bash
# Aguarde o MySQL iniciar
docker-compose logs mysql
```

### Limpar dados e reiniciar
```bash
docker-compose down -v
docker-compose up -d
```

## 📝 Licença

MIT

## 👤 Autor

Desenvolvido como Tech Challenge da Fase 1 - FIAP

---

**Última atualização**: 2026-05-03

