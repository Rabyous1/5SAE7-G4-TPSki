# Utilisation d'une image de base OpenJDK 11
FROM adoptopenjdk/openjdk11:alpine

# Création d'un répertoire de travail dans l'image
WORKDIR /app

# Copie du fichier JAR de l'application Spring Boot dans le conteneur
COPY ../../../../../target/gestion-station-ski-1.0.jar app.jar

# Commande pour exécuter l'application Spring Boot au démarrage du conteneur
CMD ["java", "-jar", "app.jar"]