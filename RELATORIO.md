# Relatório do Projeto — CRM Academia

## 1. Visão geral

Backend de um CRM para gestão de academias de artes marciais, desenvolvido como TCC. É uma API REST em **Java 21 + Spring Boot**, com persistência em **PostgreSQL** e documentação via **Swagger/OpenAPI**. O repositório é somente o backend — o frontend é um projeto separado que consome esta API.

- **Repositório:** `/home/luiz/Documentos/Projects/crm`
- **Pacote raiz:** `com.luizmrd.crm`
- **Build:** Maven (`mvnw`)
- **Commits:** 36 (primeiro em 22/08, último em 15/09)
- **Código principal:** ~2.100 linhas Java em 63 arquivos

## 2. Stack técnica (real x documentada)

| Item | Estado |
|---|---|
| Java 21 / Spring Boot | ✅ presentes |
| Spring Web MVC / Spring Data JPA | ✅ presentes |
| PostgreSQL + Hibernate | ✅ configurado (`ddl-auto: update`) |
| Lombok | ✅ em uso |
| SpringDoc/Swagger | ✅ dependência incluída, porém **sem configuração** |
| Spring Security / JWT / RBAC | ❌ **documentado no README, mas NÃO implementado** |
| Frontend | ❌ não presente neste repo |

## 3. Arquitetura

Organização em camadas, por módulo de domínio:

```
controller → service → repository (Spring Data) → entity
                 ↘ dto (request/response)
```

- **6 controllers**, **9 services** (inclui `especificacao/` com Specifications para filtros), **9 entidades JPA**, **22 DTOs**, **9 repositórios**.
- `GlobalHandlerException` (`@RestControllerAdvice`) centraliza erros (`BadRequestException`, `ResourceNotFoundException`).
- Endpoints base: `api/v1/alunos`, `/aulas`, `/contratos`, `/plano`, `/financeiro`, `/usuarios`.

## 4. Módulos implementados

- **Aluno** — CRUD parcial, busca com filtros (Specification), geração de código de acesso aleatório, snapshot do valor do plano.
- **Aula** — criação, busca por filtro, atualização, cancelamento, inscrição/remoção de aluno, lançamento de presença.
- **Contrato** — apenas `POST /contratos`.
- **Plano** — apenas `POST /plano` (catálogo).
- **Financeiro** — criar/quitar recebimento, criar contas a pagar.
- **Usuário** — criar, atualizar perfil/permissões, ativar/desativar, busca por filtro.

## 5. Estado do roadmap (README x código)

O README descreve o sistema como praticamente completo, mas o código está em estágio inicial e diverge em pontos relevantes:

| Recurso descrito no README | Situação real |
|---|---|
| Autenticação `/auth/login`, `/auth/logout`, `/auth/me` | ❌ inexistente |
| JWT + `JwtAuthFilter` + RBAC com `@PreAuthorize` | ❌ inexistente (`spring-boot-starter-security` comentado no `pom.xml`) |
| Senhas com `BCryptPasswordEncoder` | ❌ usa **SHA-256** (`CodigoHash`) |
| Regra de "DTO sempre, nunca entidade" | ⚠️ violada em `AlunoController.buscarAlunosComFiltro` (retorna `List<AlunoEntity>`) |
| Validação com `@NotBlank`/`@Email`/`@Size` + `@Valid` | ❌ nenhuma anotação (`0` ocorrências) |
| Envelope de resposta `{ "data": {...} }` | ❌ não implementado |
| Formato de erro com `code`/`message`/`details` | ⚠️ parcial (só `message`/`status`) |
| Módulo financeiro com resumo/atrasados/contas listar | ⚠️ só criar recebimento, quitar e criar conta a pagar |
| Perfis de acesso e matriz de permissões | ❌ não aplicável (sem segurança) |

## 6. Pontos de atenção / possíveis bugs

- `AlunoController.java:45` — `PUT /alunos/{id}` responde `HttpStatus.UPGRADE_REQUIRED` (**426**), claramente incorreto (deveria ser 200/204).
- `AulaController.java:54` — `PATCH /aulas` usa `@RequestParam Long id` em vez de `/{id}`; inconsistente com o restante da API.
- `AlunoController.java:28` — retorna entidades JPA diretamente, expondo o modelo de persistência (contraria o princípio central do projeto).
- `FinanceiroService` lança `RuntimeException` genérico (linhas 46 e 49) em vez das exceções customizadas, então não caem no handler global.
- `AlunoService.java:74` — `System.out.println` de debug deixado no código.
- `AlunoService` cria aluno sem `diaVencimento`/`statusPagamento` default; `contrato` é opcional.
- Sem Docker/`docker-compose`; credenciais via variáveis `DATABASE_USERNAME`/`DATABASE_PASSWORD`.
- `pom.xml` com Spring Boot `4.0.8` (versão incomum — verificar compatibilidade) e `groupId` `com.luizmrd` divergente do `com.ctbangkok.crm` do README.
- Testes: apenas `CrmApplicationTests.contextLoads` (nenhuma cobertura de negócio).
- Submódulo git `proz-backend-crm~` presente, porém praticamente vazio (só README/.gitignore).

## 7. Conclusão

Projeto em fase intermediária: a **base de domínio e CRUD já existe** (alunos, aulas, financeiro, usuários, planos), com arquitetura em camadas e specifications para filtros. Contudo, a **camada de segurança/autenticação descrita no README não foi implementada**, há ausência total de validações (`@Valid`), alguns DTOs/entidades vazando entre camadas e pequenos bugs de status HTTP. Para alinhar código e documentação, as prioridades seriam: implementar segurança (JWT + RBAC), adicionar validação nos DTOs, corrigir os endpoints inconsistentes e escrever testes.
