# Stage 1: Build the Spring Boot application using Maven
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/gestion-station-ski-1.0.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
