# CRM Academia

Backend de um sistema de gestão interna para academias de artes marciais,
desenvolvido como **Trabalho de Conclusão de Curso (TCC)**. Este repositório
contém **somente a API REST**; o frontend é um projeto separado que a consome.

## Sobre o sistema

O CRM Academia é um sistema **interno** usado pela equipe da academia para:

- Cadastrar e acompanhar alunos, com indicadores de risco de evasão.
- Gerenciar planos, contratos e endereços/contatos de emergência dos alunos.
- Criar, listar e gerenciar aulas, com inscrição de alunos e lançamento de
  presença.
- Controlar o financeiro: recebimentos de mensalidades e contas a pagar.
- Gerenciar os próprios usuários do sistema, com controle de acesso por perfil.

## Perfis de acesso

O sistema define três perfis no enum `PerfilAcessoEnum`:

| Perfil | Descrição |
|---|---|
| **ADMINISTRADOR** | Acesso total, incluindo a gestão de usuários |
| **PROFESSOR** | Perfil previsto no domínio |
| **RECEPCIONISTA** | Perfil previsto no domínio |

O único endpoint com restrição por perfil implementada é o de usuários
(`/api/v1/usuarios`), acessível apenas a **ADMINISTRADOR** via `@PreAuthorize`.
Usuários com `ativo = false` não autenticam (`UsuarioEntity.isEnabled()`).

---

## Stack utilizada

| Tecnologia | Uso |
|---|---|
| **Java 21** | Linguagem principal |
| **Spring Boot 4.0.8** | Framework base da aplicação |
| **Spring Web MVC** | Construção dos endpoints REST |
| **Spring Data JPA** | Persistência e mapeamento objeto-relacional |
| **Spring Security** | Autenticação e autorização |
| **JWT (JJWT 0.12.6)** | Autenticação stateless entre frontend e backend |
| **PostgreSQL** | Banco de dados relacional |
| **HikariCP** | Pool de conexões |
| **Lombok** | Redução de boilerplate |
| **SpringDoc OpenAPI 3.0.2** | Documentação interativa da API (Swagger UI) |

---

## Arquitetura

O código segue uma **arquitetura em camadas**, organizada em pacotes:

```
com.luizmrd.crm
  ├── controller/    # Endpoints REST
  ├── service/       # Regras de negócio (+ service/especificacao para filtros)
  ├── database/
  │     ├── model/   # Entidades JPA (+ model/enuns)
  │     └── repository/  # Repositórios Spring Data
  ├── dto/           # DTOs de request/response (aluno, aula, auth, contrato,
  │                  #   endereco, financeiro, inscricao, plano, usuario...)
  ├── commom/        # ApiResponse e PageResponse
  ├── config/        # Segurança, JWT, CORS e inicialização do admin
  ├── exception/     # Exceções e ErrorResponse
  ├── handler/       # Tratamento global de exceções
  └── util/          # Utilitários (dia de vencimento, recibo)
```

O fluxo de uma requisição é:

```
Controller → Service → Repository → Entity (banco de dados)
```

### Padrão de resposta da API

Respostas de sucesso usam o envelope `ApiResponse`:

```json
{ "data": { } }
```

Listagens paginadas usam o envelope `PageResponse`.

Respostas de erro (`ErrorResponse`), produzidas pelo `GlobalHandlerException`
(`@RestControllerAdvice`), seguem o formato:

```json
{
  "error": {
    "code": "USUARIO_OU_SENHA_INVALIDOS",
    "message": "E-mail ou senha inválidos",
    "details": null
  }
}
```

---

## Modelo de dados

Entidades JPA em `database/model`: `UsuarioEntity`, `AlunoEntity`, `PlanoEntity`,
`AulaEntity`, `InscricaoEntity`, `ContratoEntity`, `RecebimentoEntity`,
`ContasPagarEntity`, `HistoricoPagamentoEntity`, `EnderecoEntity` e
`ContatoEmergenciaEntity`.

Destaques de modelagem:

- **Plano como catálogo com valor copiado por aluno**: o aluno guarda o campo
  `valorMensal`, separado do `valorPadrao` do plano.
- **Inscrição como entidade própria** (não `@ManyToMany`) entre `AlunoEntity` e
  `AulaEntity`, com `presencaStatus` e `dataInscricao`.
- **Professor como campo texto na `AulaEntity`**, não uma chave estrangeira.
- **Aluno com código de acesso curto** gerado por `CodigoAcessoCurto`.

---

## Autenticação e autorização

A API é **stateless**: o login gera um token JWT que deve ser enviado no header
`Authorization: Bearer {token}` em todas as rotas protegidas.

- Senhas são armazenadas com `BCryptPasswordEncoder`.
- O token carrega `nome` e `perfil` (`cargo`) do usuário como claims.
- Um filtro (`JwtAuthenticationFilter`) valida o token e popula o contexto de
  segurança do Spring a cada requisição.
- Rotas públicas: `POST /api/v1/auth/login` e os caminhos do Swagger
  (`/v3/api-docs/**`, `/swagger-ui/**`, `/swagger-ui.html`). O restante exige
  autenticação.
- O controle de acesso por perfil é feito via `@PreAuthorize` (aplicado ao
  `UsuarioController`).

Na primeira execução, o `AdminInitializer` cria um usuário administrador caso
não exista nenhum, usando as variáveis `ADMIN_NOME`, `ADMIN_EMAIL` e
`ADMIN_SENHA`.

---

## Endpoints da API

Prefixo geral: `/api/v1`.

### Autenticação (`/auth`)

| Método | Rota |
|---|---|
| POST | `/auth/login` |
| GET | `/auth/me` |
| POST | `/auth/logout` |

### Alunos (`/alunos`)

| Método | Rota |
|---|---|
| GET | `/alunos` (com filtros) |
| GET | `/alunos/cadastrados` |
| GET | `/alunos/risco-evasao` |
| GET | `/alunos/{id}` |
| POST | `/alunos` |
| PUT | `/alunos/{id}` |

### Aulas (`/aulas`)

| Método | Rota |
|---|---|
| GET | `/aulas` (com filtros) |
| GET | `/aulas/{id}` |
| POST | `/aulas` |
| PATCH | `/aulas?id={id}` |
| POST | `/aulas/{id}/cancelar` |
| POST | `/aulas/inscrever?aulaId=&alunoId=` |
| DELETE | `/aulas/cancelar-inscricao?aulaId=&alunoId=` |
| POST | `/aulas/lancar-presenca/{id}` |

### Financeiro (`/financeiro`)

| Método | Rota |
|---|---|
| POST | `/financeiro/recebimento` |
| POST | `/financeiro/recebimento/quitar/{id}` |
| GET | `/financeiro/recebimentos` |
| GET | `/financeiro/recebimentos/atrasados` |
| GET | `/financeiro/recebimentos/aluno/{id}` |
| POST | `/financeiro/contas-pagar` |
| POST | `/financeiro/contas-pagar/quitar/{id}` |
| GET | `/financeiro/contas-pagar` |
| GET | `/financeiro/resumo` |
| GET | `/financeiro/alunos/{id}/situacao-financeira` |

### Planos (`/plano`)

| Método | Rota |
|---|---|
| GET | `/plano` |
| GET | `/plano/{id}` |
| POST | `/plano` |
| PUT | `/plano/{id}` |
| PATCH | `/plano/{id}` |
| DELETE | `/plano/{id}` |
| PATCH | `/plano/vincular-plano/aluno/{alunoId}/plano/{planoId}` |

### Contratos (`/contratos`)

| Método | Rota |
|---|---|
| GET | `/contratos` |
| GET | `/contratos/{id}` |
| GET | `/contratos/aluno/{alunoId}` |
| POST | `/contratos` |
| PUT | `/contratos/{id}` |
| PATCH | `/contratos/{id}` |
| DELETE | `/contratos/{id}` |

### Contatos de emergência (`/contatos-emergencia`)

| Método | Rota |
|---|---|
| GET | `/contatos-emergencia/{id}` |
| GET | `/contatos-emergencia/aluno/{alunoId}` |
| POST | `/contatos-emergencia` |
| PUT | `/contatos-emergencia/{id}` |
| PATCH | `/contatos-emergencia/{id}` |

### Endereços (`/enderecos`)

| Método | Rota |
|---|---|
| GET | `/enderecos/{id}` |
| GET | `/enderecos/aluno/{alunoId}` |
| POST | `/enderecos` |
| PUT | `/enderecos/{id}` |
| PATCH | `/enderecos/{id}` |

### Usuários (`/usuarios`) — restrito a ADMINISTRADOR

| Método | Rota |
|---|---|
| GET | `/usuarios/{id}` |
| GET | `/usuarios/buscar` |
| POST | `/usuarios` |
| PUT | `/usuarios/atualizar-perfil/{id}` |
| PATCH | `/usuarios/atualizar-permissoes/{id}` |
| PATCH | `/usuarios/{id}/ativar` |
| PATCH | `/usuarios/{id}/desativar` |

A documentação interativa completa (com schemas de request/response) fica
disponível via Swagger UI após subir a aplicação.

---

## Como executar o projeto

### Pré-requisitos

- Java 21
- Maven (ou usar o wrapper `./mvnw`)
- PostgreSQL em execução

### Configuração

A aplicação lê as configurações de variáveis de ambiente (ver
`src/main/resources/application.yaml` e o arquivo `.env`):

| Variável | Descrição |
|---|---|
| `DB_HOST` | Host do PostgreSQL |
| `DB_PORT` | Porta do PostgreSQL |
| `DB` | Nome do banco |
| `DATABASE_USERNAME` | Usuário do banco |
| `DATABASE_PASSWORD` | Senha do banco |
| `JWT_SECRET` | Chave secreta do JWT |
| `JWT_EXPIRACAO_SEGUNDOS` | Tempo de expiração do token (segundos) |
| `ADMIN_NOME` | Nome do administrador inicial |
| `ADMIN_EMAIL` | E-mail do administrador inicial |
| `ADMIN_SENHA` | Senha do administrador inicial |

O Hibernate está configurado com `ddl-auto: update`, criando/atualizando as
tabelas automaticamente.

### Executando localmente

```bash
./mvnw spring-boot:run
```

### Executando com Docker

O projeto inclui `Dockerfile` e `docker-compose.yml`, que sobem o banco
PostgreSQL e a aplicação:

```bash
docker compose up --build
```

### Documentação da API

```
http://localhost:8080/swagger-ui.html
```
