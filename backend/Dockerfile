# Use Eclipse Temurin Java 17 runtime (official replacement for openjdk)
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the JAR file from build/libs folder
COPY build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]