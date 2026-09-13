# CRM Academia

Sistema de gestão interna para academias de artes marciais, desenvolvido como
**Trabalho de Conclusão de Curso (TCC)**. O projeto contempla uma aplicação
full stack completa — frontend e backend — simulando um CRM real usado por
administradores, professores e recepcionistas para gerenciar alunos, aulas,
matrículas e o financeiro de uma academia.

## Objetivo acadêmico

Este projeto foi pensado para demonstrar, na prática, boas práticas de
arquitetura e desenvolvimento backend com Java e Spring, entre elas:

- **Arquitetura em camadas** (Controller → Service → Repository), com
  responsabilidades bem separadas entre cada camada.
- **Uso de DTOs (Data Transfer Objects)** para nunca expor entidades JPA
  diretamente na API — toda entrada e saída de dados passa por objetos de
  transferência dedicados, protegendo informações sensíveis (como senha de
  usuário) e desacoplando o modelo de persistência do contrato exposto ao
  frontend.
- **Autenticação e autorização stateless** com JWT, incluindo controle de
  acesso por perfil (RBAC).
- **Tratamento centralizado de exceções**, com um formato de erro padronizado
  em toda a API.
- **Documentação de API viva** via OpenAPI/Swagger, gerada a partir do
  próprio código.

## Sobre o sistema

O CRM Academia é um sistema **interno** (não voltado ao aluno final) usado
pela equipe da academia para:

- Cadastrar, editar e acompanhar alunos, incluindo indicadores de risco de
  evasão (frequência, dias sem treino).
- Realizar matrículas completas — cadastro do aluno, geração de contrato e
  processamento do primeiro pagamento em um único fluxo.
- Criar, editar e gerenciar aulas, com controle de conflito de horário
  (professor e aluno), limite de vagas e lançamento de frequência/presença.
- Controlar o financeiro da academia: recebimentos de mensalidades, contas a
  pagar e um resumo consolidado de fluxo de caixa.
- Gerenciar os próprios usuários do sistema (administradores, professores e
  recepcionistas), com controle de acesso por perfil.

## Perfis de acesso

O sistema define três perfis, cada um com um recorte de permissões distinto:

| Perfil | Acesso |
|---|---|
| **Administrador** | Acesso total: financeiro completo, relatórios e gestão de usuários |
| **Professor** | Agenda de aulas, frequência dos alunos e consulta de turmas — restrito às próprias aulas |
| **Recepcionista** | Matrículas, inscrições, recebimento de mensalidades e ficha cadastral de alunos |

Usuários com status **Inativo** não conseguem autenticar nem executar
nenhuma rota protegida.

---

## Stack utilizada

### Backend

| Tecnologia | Uso |
|---|---|
| **Java 21** | Linguagem principal |
| **Spring Boot** | Framework base da aplicação |
| **Spring Web** | Construção dos endpoints REST |
| **Spring Data JPA** | Persistência e mapeamento objeto-relacional |
| **Spring Security** | Autenticação e autorização (RBAC) |
| **JWT (JSON Web Token)** | Autenticação stateless entre frontend e backend |
| **PostgreSQL** | Banco de dados relacional |
| **Lombok** | Redução de boilerplate (getters, setters, construtores) |
| **SpringDoc / Swagger** | Documentação interativa da API (OpenAPI) |

### Frontend

Aplicação separada, consumindo a API REST documentada neste repositório.

---

## Arquitetura

O backend segue uma **arquitetura em camadas**, organizada por módulo de
domínio (e não por camada técnica global), facilitando a navegação em um
projeto com múltiplos contextos de negócio:

```
com.ctbangkok.crm
  ├── auth/          # Login, JWT, filtro de autenticação
  ├── aluno/         # Cadastro e gestão de alunos
  ├── matricula/     # Matrícula, contrato e processamento de pagamento
  ├── aula/          # Aulas, inscrições e frequência
  ├── financeiro/    # Recebimentos e contas a pagar
  ├── usuario/       # Gestão de usuários do sistema
  ├── common/        # Exceções, resposta padrão da API, utilitários compartilhados
  └── config/        # Configuração de segurança, CORS, Swagger
```

Cada módulo segue o fluxo:

```
Controller → Service → Repository → Entity (banco de dados)
     ↑                        
    DTO (request/response)     
```

O **Controller** nunca recebe nem devolve uma entidade JPA diretamente —
sempre um DTO. Isso garante que:

- Campos sensíveis (como senha) nunca vazam na resposta da API.
- Mudanças no modelo de persistência não quebram automaticamente o contrato
  exposto ao frontend.
- Regras de validação de entrada ficam isoladas nos DTOs de request
  (`@NotBlank`, `@Email`, `@Size`, etc.), sem contaminar as entidades.

### Padrão de resposta da API

Toda resposta de sucesso segue o envelope:

```json
{ "data": { } }
```

Toda resposta de erro segue um formato padronizado, com código semântico e
mensagem legível:

```json
{
  "error": {
    "code": "AULA_CONFLITO_PROFESSOR",
    "message": "O professor já possui uma aula agendada neste horário.",
    "details": { }
  }
}
```

Esse tratamento é centralizado via `@RestControllerAdvice`, evitando
duplicação de lógica de erro em cada controller.

---

## Modelo de dados

O domínio principal é composto pelas entidades: `Usuario`, `Aluno`, `Plano`,
`Aula`, `Inscricao` (associação entre aluno e aula, com controle de
presença), `Contrato`, `Recebimento`, `ContaPagar` e `HistoricoPagamento`.

Destaques de modelagem:

- **Plano como catálogo com preço "congelado" por aluno**: o valor mensal do
  aluno é copiado do plano no momento da matrícula (snapshot), para que um
  reajuste de preço no catálogo não afete retroativamente quem já está
  matriculado.
- **Inscrição como entidade própria** (não `@ManyToMany` simples) entre
  `Aluno` e `Aula`, permitindo guardar status de presença e data de
  inscrição.
- **Professor como campo texto na `Aula`**, não uma chave estrangeira para
  `Usuario`, seguindo o contrato de dados definido com o frontend.

---

## Autenticação e autorização

A API é **stateless**: o login gera um token JWT que deve ser enviado no
header `Authorization: Bearer {token}` em todas as rotas protegidas.

- Senhas são armazenadas com hash (`BCryptPasswordEncoder`), nunca em texto
  puro.
- O token carrega o `perfilAcesso` do usuário como claim, usado por um filtro
  (`JwtAuthFilter`) para popular o contexto de segurança do Spring a cada
  requisição.
- O controle de acesso por perfil é feito via `@PreAuthorize` nos
  controllers, refletindo a matriz de permissões descrita acima.
- Regras de negócio mais específicas (ex: um professor só pode alterar as
  próprias aulas) são validadas na camada de serviço, não apenas por
  anotação.

---

## Endpoints da API

### Autenticação (`/api/v1/auth`)

| Método | Rota | Acesso |
|---|---|---|
| POST | `/auth/login` | Público |
| POST | `/auth/logout` | Autenticado |
| GET | `/auth/me` | Autenticado |

### Alunos (`/api/v1/alunos`)

| Método | Rota | Acesso |
|---|---|---|
| GET | `/alunos` | Administrador, Professor, Recepcionista |
| GET | `/alunos/risco-evasao` | Administrador, Professor, Recepcionista |
| GET | `/alunos/{id}` | Administrador, Professor, Recepcionista |
| POST | `/alunos` | Administrador, Recepcionista |
| PATCH / PUT | `/alunos/{id}` | Administrador, Recepcionista |

### Matrícula (`/api/v1`)

| Método | Rota | Acesso |
|---|---|---|
| POST | `/matriculas` | Administrador, Recepcionista |
| POST | `/pagamentos/processar` | Administrador, Recepcionista |
| POST | `/contratos` | Administrador, Recepcionista |

### Aulas (`/api/v1/aulas`)

| Método | Rota | Acesso |
|---|---|---|
| GET | `/aulas` | Todos os perfis |
| GET | `/aulas/{id}` | Todos os perfis |
| POST | `/aulas` | Administrador, Professor |
| PATCH | `/aulas/{id}` | Administrador, Professor (próprias aulas) |
| POST | `/aulas/{id}/cancelar` | Administrador, Professor (próprias aulas) |
| POST | `/aulas/{id}/inscricoes` | Todos os perfis |
| DELETE | `/aulas/{id}/inscricoes/{alunoId}` | Todos os perfis |
| POST | `/aulas/{id}/frequencias` | Todos os perfis |

### Financeiro (`/api/v1/financeiro`)

| Método | Rota | Acesso |
|---|---|---|
| GET | `/financeiro/resumo` | Administrador (completo), Recepcionista (recebimentos) |
| GET | `/financeiro/recebimentos` | Administrador, Recepcionista |
| GET | `/financeiro/recebimentos/atrasados` | Administrador, Recepcionista |
| POST | `/financeiro/recebimentos` | Administrador, Recepcionista |
| POST | `/financeiro/recebimentos/{id}/quitar` | Administrador, Recepcionista |
| GET | `/financeiro/contas-pagar` | Administrador |
| POST | `/financeiro/contas-pagar` | Administrador |
| POST | `/financeiro/contas-pagar/{id}/quitar` | Administrador |

### Usuários (`/api/v1/usuarios`)

Módulo restrito exclusivamente ao perfil **Administrador**.

| Método | Rota |
|---|---|
| GET | `/usuarios` |
| GET | `/usuarios/{id}` |
| POST | `/usuarios` |
| PUT / PATCH | `/usuarios/{id}` |
| PATCH | `/usuarios/{id}/status` |
| DELETE | `/usuarios/{id}` |

A documentação interativa completa (com schemas de request/response) fica
disponível via Swagger UI após subir a aplicação — ver seção abaixo.

---

## Como executar o projeto

### Pré-requisitos

- Java 21
- Maven
- PostgreSQL em execução local (ou via Docker)

### Configuração

1. Crie um banco PostgreSQL para o projeto.
2. Configure as variáveis de ambiente (ou edite `application.yml`):

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/crm_academia
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:postgres}

app:
  jwt:
    secret: ${JWT_SECRET:troque-por-uma-string-aleatoria-de-32-ou-mais-caracteres}
    expiracao-segundos: 28800
```

3. Rode a aplicação:

```bash
./mvnw spring-boot:run
```

4. Acesse a documentação da API:

```
http://localhost:8080/swagger-ui.html
```

---

## Status do projeto

Projeto em desenvolvimento como parte do Trabalho de Conclusão de Curso.
Módulos e funcionalidades são implementados de forma incremental, seguindo a
ordem: entidades → segurança/autenticação → módulos de negócio (Alunos,
Matrícula, Aulas, Financeiro, Usuários) → refinamento de validações.
