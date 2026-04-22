# ---- Stage 1: Build the JAR using Maven ----
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copy all project files
COPY . .

# Build the project and skip tests
RUN mvn clean package -DskipTests

# ---- Stage 2: Run the JAR ----
FROM eclipse-temurin:17-jdk
WORKDIR /app

# Copy the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar

# Start the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose port 8080
EXPOSE 8080