# Estágio 1: Build
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Define o diretório de trabalho ANTES de qualquer cópia
WORKDIR /app

# Primeiro copia apenas o pom.xml para baixar as dependências (melhora o cache)
COPY pom.xml .
RUN mvn dependency:go-offline

# AGORA copia a pasta src inteira
COPY src ./src

# Build do JAR
RUN mvn clean package -DskipTests

# Estágio 2: Execução
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copia o JAR gerado no estágio anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]