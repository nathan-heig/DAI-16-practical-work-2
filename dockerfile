# Utiliser une image de base Java
FROM openjdk:22

# Définir le répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR de l'application dans le conteneur
COPY target/pratical_work_01-1.0-SNAPSHOT.jar app.jar
COPY data data

# Définir la commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]