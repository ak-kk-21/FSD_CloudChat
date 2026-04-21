# Use Eclipse Temurin Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy gradle files
COPY build.gradle .
COPY settings.gradle .
COPY gradle gradle
COPY gradlew gradlew
COPY gradlew.bat gradlew.bat

# Copy source code
COPY src src

# Make gradlew executable
RUN chmod +x gradlew

# Download gradle wrapper if needed
RUN apk add --no-cache curl
RUN ./gradlew --version || true

# Build the application (skip tests)
RUN ./gradlew build -x test

# Copy the built JAR to a fixed name
RUN cp build/libs/*.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
