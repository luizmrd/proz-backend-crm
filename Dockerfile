# ==============================================================================
# Etapa 1: Build da aplicação (Maven + JDK 21)
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Define o diretório de trabalho do container
WORKDIR /app

# Copia o arquivo pom.xml para baixar as dependências primeiro (aproveitamento do cache do Docker)
COPY pom.xml .

# Baixa as dependências do Maven sem empacotar ainda
RUN mvn dependency:go-offline -B

# Copia todo o código fonte da aplicação
COPY src ./src

# Compila e empacota a aplicação ignorando os testes de unidade no build da imagem
RUN mvn clean package -DskipTests

# ==============================================================================
# Etapa 2: Imagem final de execução (JRE 21 Leve)
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho na imagem final
WORKDIR /app

# Cria um usuário não-root por questões de segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copia o arquivo .jar gerado na etapa de build
COPY --from=build /app/target/*.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Variáveis de ambiente configuráveis (Segredos do JWT e do Banco de Dados)
ENV JWT_SECRET="troque-por-uma-string-aleatoria-de-32-ou-mais-caracteres" \
    SPRING_PROFILES_ACTIVE="prod"

# Comando de entrada para executar a aplicação Java
ENTRYPOINT ["java", "-jar", "app.jar"]