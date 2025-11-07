# Use a lightweight OpenJDK 21 image
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle/wrapper/gradle-wrapper.jar gradle/wrapper/

# Copy the build scripts and settings
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src ./src

# Build the application
# This step will download dependencies and build the JAR
RUN ./gradlew bootJar

# Expose the port that Spring Boot runs on
EXPOSE 8080

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "build/libs/github-cicd-demo-0.0.1-SNAPSHOT.jar"]
