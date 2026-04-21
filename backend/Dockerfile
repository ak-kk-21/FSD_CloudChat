# Use Eclipse Temurin Java 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy only the files needed for dependency resolution
COPY build.gradle .
COPY settings.gradle .

# Copy source code
COPY src src

# Create gradle wrapper directory and download wrapper
RUN mkdir -p gradle/wrapper
RUN apk add --no-cache curl
RUN curl -L -o gradle/wrapper/gradle-wrapper.jar https://github.com/gradle/gradle/raw/v8.5.0/gradle/wrapper/gradle-wrapper.jar

# Create gradle-wrapper.properties
RUN echo "distributionBase=GRADLE_USER_HOME" > gradle/wrapper/gradle-wrapper.properties && \
    echo "distributionPath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties && \
    echo "distributionUrl=https\://services.gradle.org/distributions/gradle-8.5-bin.zip" >> gradle/wrapper/gradle-wrapper.properties && \
    echo "zipStoreBase=GRADLE_USER_HOME" >> gradle/wrapper/gradle-wrapper.properties && \
    echo "zipStorePath=wrapper/dists" >> gradle/wrapper/gradle-wrapper.properties

# Create a simple gradlew script
RUN echo '#!/bin/sh' > gradlew && \
    echo 'java -cp gradle/wrapper/gradle-wrapper.jar org.gradle.wrapper.GradleWrapperMain "$@"' >> gradlew && \
    chmod +x gradlew

# Build the application
RUN ./gradlew build -x test

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "build/libs/*.jar"]
