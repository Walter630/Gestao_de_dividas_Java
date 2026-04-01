# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copia o pom.xml primeiro para cache de dependências
COPY pom.xml .
RUN mvn dependency:go-offline -B

# 2. Copia o código fonte
COPY src ./src

# 3. Build do projeto
RUN mvn clean package -DskipTests -B

# Estágio Final
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 4. Copia o JAR correto
COPY --from=build /app/target/Gestao_de_Contas-0.0.1-SNAPSHOT.jar app.jar

# Debug: verifica se o JAR existe
RUN ls -la /app/app.jar || echo "JAR NOT FOUND!"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]