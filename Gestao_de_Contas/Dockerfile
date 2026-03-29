# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia o pom.xml
COPY pom.xml .
RUN mvn dependency:go-offline

# AQUI ESTÁ O PULO DO GATO: Use "Src" com S maiúsculo
COPY Src ./Src

# No comando do Maven, ele vai procurar o código dentro de Src agora
RUN mvn clean package -DskipTests

# Estágio Final
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]