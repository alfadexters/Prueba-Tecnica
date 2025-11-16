# Etapa 1: build de la aplicación con Maven
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copiamos pom.xml primero para aprovechar cache de dependencias
COPY pom.xml .

# Descargamos dependencias (sin compilar todavía)
RUN mvn -B dependency:go-offline

# Copiamos el código fuente
COPY src ./src

# Empaquetamos la app (sin ejecutar tests en la imagen)
RUN mvn -B clean package -DskipTests

# Etapa 2: imagen final para correr la app
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copiamos el jar construido desde la etapa anterior
COPY --from=build /app/target/prueba-tecnica-0.0.1-SNAPSHOT.jar app.jar

# Puerto donde expone Spring Boot
EXPOSE 8080

# Comando de arranque
ENTRYPOINT ["java", "-jar", "app.jar"]
