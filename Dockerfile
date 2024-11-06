# Dockerfile
FROM openjdk:17-jdk-alpine
VOLUME /tmp

COPY gestion-station-ski-1.0.jar /app/app.jar
WORKDIR /app
ENTRYPOINT ["java   ", "-jar", "app.jar"]