# ==============================================================================
# Etapa 1: Build da aplicação (Maven + JDK 21)
# ==============================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests
# ==============================================================================
# Etapa 2: Imagem final de execução (JRE 21 Leve)
# ==============================================================================
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE="prod"

ENTRYPOINT ["java", "-jar", "app.jar"]