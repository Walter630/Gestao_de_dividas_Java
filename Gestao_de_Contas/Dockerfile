# Estágio de Build
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copia tudo do seu GitHub para o container
COPY . .

# 2. ENTRA na pasta onde o pom.xml realmente está
# O comando 'cd' garante que o Maven rode no lugar certo
RUN cd Gestao_de_dividas_Java && mvn clean package -DskipTests

# Estágio Final
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# 3. Busca o JAR dentro da pasta target da subpasta
COPY --from=build /app/Gestao_de_dividas_Java/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]