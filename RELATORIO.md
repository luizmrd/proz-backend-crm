# Relatório do Projeto — CRM Academia

## 1. Visão Geral

Backend de um sistema de gestão interna (CRM) para academias de artes marciais, desenvolvido como **Trabalho de Conclusão de Curso (TCC)**. Trata-se de uma API REST stateless em **Java 21 + Spring Boot**, com persistência em **PostgreSQL**, autenticação via **JWT**, documentação interativa com **Swagger/OpenAPI** e containerização com **Docker**.

- **Repositório:** `/home/luiz/Documentos/Projects/crm`
- **Pacote raiz:** `com.luizmrd.crm`
- **Gerenciador de build:** Maven (`./mvnw`)
- **Histórico Git:** 67 commits (início em 22/08/2026)
- **Volume de código:** ~4.250 linhas de código Java distribuídas em 116 arquivos

---

## 2. Stack Tecnológica e Infraestrutura

| Tecnologia / Recurso | Versão / Configuração | Estado Atual |
|---|---|---|
| **Java** | 21 (com **Virtual Threads** habilitadas) | ✅ Configurado (`spring.threads.virtual.enabled=true`) |
| **Spring Boot** | 4.0.8 (`spring-boot-starter-parent`) | ✅ Em uso |
| **Spring Web MVC** | REST API (`/api/v1/...`) | ✅ Implementado (9 controllers) |
| **Spring Data JPA / Hibernate** | `ddl-auto: update`, `default_batch_fetch_size: 50` | ✅ Implementado (11 entidades, 11 repositórios, 4 Specifications) |
| **PostgreSQL + HikariCP** | Pool: `max=10`, `min-idle=2`, `prepareThreshold=0` | ✅ Configurado |
| **Spring Security + JJWT** | `0.12.6` (Stateless + `BCryptPasswordEncoder` + RBAC) | ✅ Implementado |
| **SpringDoc OpenAPI (Swagger)** | `3.0.2` com esquema Bearer JWT (`OpenApiConfig`) | ✅ Configurado |
| **Lombok** | Redução de boilerplate (`@Builder`, `@Getter`, `@Setter`) | ✅ Em uso |
| **Docker / Docker Compose** | Multi-stage build (JDK 21 Alpine → JRE 21 Alpine) + Postgres 16 | ✅ Configurado |

---

## 3. Arquitetura e Organização de Pacotes

O sistema segue a arquitetura em camadas (`Controller → Service → Repository → Entity`), organizada sob `com.luizmrd.crm`:

- **`controller/` (9 classes):** Exposição das rotas REST sob o prefixo `/api/v1` com uso de DTOs e envelopes `ApiResponse<T>` e `PageResponse<T>`.
- **`service/` (9 classes + 4 Specifications):** Regras de negócio, cálculos financeiros, análise de risco de evasão e filtros dinâmicos (`AlunoEspecificacao`, `AulaEspecificacao`, `RecebimentoEspecificacao`, `UsuarioEspecificacao`).
- **`database/model/` (11 entidades + 9 enums):** Modelo de domínio relacional mapeado via JPA (`AlunoEntity`, `AulaEntity`, `InscricaoEntity`, `PlanoEntity`, `ContratoEntity`, `RecebimentoEntity`, `ContasPagarEntity`, `HistoricoPagamentoEntity`, `EnderecoEntity`, `ContatoEmergenciaEntity`, `UsuarioEntity`).
- **`database/repository/` (11 interfaces):** Repositórios Spring Data JPA com suporte a `JpaSpecificationExecutor` e queries customizadas (`@Query`).
- **`dto/` (45 classes/records):** Objetos de transferência de dados organizados por subpacote de domínio (`aluno`, `aula`, `auth`, `contatoemergencia`, `contrato`, `endereco`, `financeiro`, `inscricao`, `plano`, `usuario`).
- **`config/` (7 classes):** Segurança (`SecurityConfig`, `JwtService`, `JwtAuthenticationFilter`), bootstrap de usuário admin (`AdminInitializer`), documentação (`OpenApiConfig`) e geradores (`CodigoAcessoCurto`, `CodigoHash`).
- **`exception/` e `handler/` (7 classes):** Tratamento centralizado de erros (`GlobalHandlerException`, `RestAuthenticationEntryPoint`, `RestAccessDeniedHandler`) retornando o padrão `ErrorResponse`.
- **`commom/` e `util/` (4 classes):** Envelopes padronizados de resposta (`ApiResponse`, `PageResponse`) e utilitários (`DiaVencimentoUtil`, `ReciboUtil`).

---

## 4. Módulos e Funcionalidades Implementadas

1. **Autenticação e Segurança (`/api/v1/auth`):**
   - Login (`POST /login`) gerando token JWT stateless com claims de `nome` e `perfil`.
   - Consulta de usuário logado (`GET /me`) e endpoint de logout (`POST /logout`).
   - Criação automática de administrador inicial via `AdminInitializer`.
2. **Alunos (`/api/v1/alunos`):**
   - Criação de aluno com geração de código de acesso curto único (8 caracteres) e snapshot do `valorPadrao` do plano para `valorMensal`.
   - Busca com filtros dinâmicos (Specification) + filtro de risco de evasão.
   - Monitoramento de **risco de evasão** (`GET /risco-evasao`) baseado em mensalidade atrasada (`MENSALIDADE_ATRASADA`) e ausência de treinos há mais de 15 dias (`SEM_FREQUENCIA`).
   - Listagem geral (`/cadastrados`), busca por ID e atualização de perfil.
3. **Aulas, Inscrições e Presenças (`/api/v1/aulas`):**
   - Criação, atualização parcial, cancelamento e busca de aulas com filtros.
   - Inscrição e cancelamento de inscrição de alunos em aulas, além de lançamento de presença (`PRESENTE`).
4. **Financeiro (`/api/v1/financeiro`):**
   - **Recebimentos:** Criação vinculada ao vencimento do contrato, quitação com emissão de recibo e atualização do status do aluno, listagem com filtros, atrasados e por aluno.
   - **Contas a pagar:** Criação, quitação com geração de comprovante e listagem de pendentes/aguardando.
   - **Fluxo de caixa e Situação Financeira:** Resumo mensal (`GET /resumo` com total recebido, pendente, despesas e saldo líquido) e situação financeira individual do aluno (`GET /alunos/{id}/situacao-financeira`).
5. **Planos (`/api/v1/plano`) e Contratos (`/api/v1/contratos`):**
   - CRUD completo com paginação (`PageResponse`) e vínculo de novo plano ao aluno (`PATCH /plano/vincular-plano/aluno/{alunoId}/plano/{planoId}`).
6. **Endereços (`/api/v1/enderecos`) e Contatos de Emergência (`/api/v1/contatos-emergencia`):**
   - Cadastro, consulta por ID ou por `alunoId`, e atualização (`PUT`/`PATCH`).
7. **Usuários (`/api/v1/usuarios`):**
   - Restrito ao perfil `ADMINISTRADOR` via `@PreAuthorize("hasRole('ADMINISTRADOR')")`.
   - Criação com hash BCrypt, atualização de perfil e permissões, ativação/desativação e busca com filtros.

---

## 5. Evolução Recente do Projeto

Em comparação com a primeira versão deste relatório (16/09), o projeto evoluiu significativamente:
- **Segurança completa implementada:** Spring Security, filtro JWT (`JwtAuthenticationFilter`), senhas com `BCryptPasswordEncoder`, handlers customizados para 401/403 e proteção de rotas.
- **Padronização de respostas:** Todos os controllers passaram a utilizar `ApiResponse<T>` e `PageResponse<T>` sem expor entidades JPA diretamente nas respostas.
- **Expansão de módulos:** Implementação completa dos CRUDs de `Plano`, `Contrato`, `Endereco`, `ContatoEmergencia`, novas rotas financeiras (`/resumo`, `/atrasados`, `/situacao-financeira`) e cálculo de risco de evasão de alunos.
- **Infraestrutura e Performance:** Adição de `Dockerfile` multi-stage, `docker-compose.yml`, configuração do Swagger (`OpenApiConfig`), ajuste de pool HikariCP, batch fetching do Hibernate e **Virtual Threads** do Java 21.

---

## 6. Melhorias Recentes Aplicadas

1. **Status HTTP e Imports Corrigidos (`fix/controllers-status-e-imports`):**
   - `PUT /api/v1/alunos/{id}` em `AlunoController` corrigido de `426 UPGRADE_REQUIRED` para `200 OK`.
   - Removido o import incorreto `org.apache.coyote.BadRequestException` em `FinanceiroController`.
2. **Padronização de Exceções e Limpeza (`fix/padronizacao-excecoes-services`):**
   - Substituídas ocorrências de `RuntimeException` genérica por `ResourceNotFoundException` (404) em `FinanceiroService`, `AulaService` e `ContratoService`.
   - Ajustadas validações de conflito/regra de negócio para lançar `BadRequestException` (400) em `AlunoService`, `AulaService` e `UsuarioService`, e `ResourceNotFoundException` (404) em `PlanoService`.
   - Removido log de debug `System.out.println` em `AlunoService`.
3. **Validação de Entrada com Bean Validation (`feat/validacao-dtos`):**
   - Adicionado `spring-boot-starter-validation` ao `pom.xml`, anotações `@NotBlank`, `@NotNull`, `@Email`, `@Positive`, `@Min`/`@Max` nos DTOs de requisição, `@Valid` nos controllers e tratamento de `MethodArgumentNotValidException` no `GlobalHandlerException`.
4. **Cobertura de Testes Unitários (`test/testes-unitarios`):**
   - Adicionados testes unitários com JUnit 5 e Mockito para `AlunoServiceTest`, `AulaServiceTest` e `FinanceiroServiceTest` (10 cenários de negócio testados e aprovados).
