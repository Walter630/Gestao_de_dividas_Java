# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copia ABSOLUTAMENTE TUDO o que está na raiz do seu Git para dentro do Docker
COPY . .

# 2. Roda o build (o Maven vai encontrar o pom.xml e a pasta Src automaticamente)
RUN mvn clean package -DskipTests

# Estágio Final
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 3. Copia o JAR que foi gerado lá na pasta target
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]