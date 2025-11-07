# Use a lightweight OpenJDK 21 image
FROM eclipse-temurin:21-jdk

# Docker 빌드 시 주입될 인자를 선언합니다.
ARG COMMIT_ID

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper files
COPY gradlew .
COPY gradle ./gradle

# Copy the build scripts and settings
COPY build.gradle settings.gradle ./

# Copy the source code
COPY src ./src

# Build the application
# This step will download dependencies and build the JAR
RUN chmod +x ./gradlew
# -PcommitId=$COMMIT_ID : Gradle 프로젝트에 commitId 속성을 전달합니다.
RUN ./gradlew bootJar -PcommitId=$COMMIT_ID

# Expose the port that Spring Boot runs on
EXPOSE 8080

# Define the entry point to run the application
ENTRYPOINT ["java", "-jar", "build/libs/github-cicd-demo-0.0.1-SNAPSHOT.jar"]
