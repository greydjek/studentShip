# Этап сборки
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -Pproduction -DskipTests

# Этап запуска
FROM eclipse-temurin:21-jdk-jammy
WORKDIR /app
COPY --from=build /app/target/*.war app.war
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.war"]