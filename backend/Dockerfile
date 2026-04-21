# Use Eclipse Temurin Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy Gradle wrapper and build files
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Grant execute permission to gradlew
RUN chmod +x gradlew

# Build the application (skip tests for faster build)
RUN ./gradlew build -x test

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "build/libs/*.jar"]
