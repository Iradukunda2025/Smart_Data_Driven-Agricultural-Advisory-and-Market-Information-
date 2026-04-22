# ---- Stage 1: Build ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# ---- Stage 2: Run ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# 👇 Replace with your actual JAR name from pom.xml
COPY --from=build /app/target/your-app-name-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080