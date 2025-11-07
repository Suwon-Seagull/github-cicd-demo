# CI/CD 실습 계획 (GitHub Actions)

이 문서는 GitHub Actions를 사용하여 Spring Boot 프로젝트에 CI/CD 파이프라인을 구축하기 위한 실습 계획을 설명합니다.

---

## 🎯 목표

*   GitHub Actions를 사용하여 Java/Gradle 프로젝트 빌드.
*   빌드된 애플리케이션을 Docker 이미지로 빌드.
*   Docker 이미지를 Docker Hub에 푸시.

---

## 🛠️ 전제 조건

*   **GitHub 계정**: 이 프로젝트가 호스팅된 GitHub 리포지토리.
*   **Docker Hub 계정**: Docker 이미지를 푸시할 Docker Hub 계정.
*   **GitHub Secrets 설정**: Docker Hub 인증 정보를 GitHub 리포지토리 Secrets에 저장해야 합니다. (아래 "GitHub Secrets 설정" 섹션 참조)

---

## 📝 구현 단계

### 1. `Dockerfile` 생성

Spring Boot 애플리케이션을 Docker 이미지로 빌드하기 위한 `Dockerfile`을 프로젝트 루트 디렉토리에 생성합니다.

**파일 경로**: `./Dockerfile`

**내용**:
```dockerfile
# Use a lightweight OpenJDK 17 image
FROM openjdk:17-jdk-slim

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
ENTRYPOINT ["java", "-jar", "build/libs/power-plant-0.0.1-SNAPSHOT.jar"]
```

### 2. GitHub Actions 워크플로우 파일 생성

CI/CD 파이프라인을 정의하는 YAML 파일을 `.github/workflows` 디렉토리 내에 생성합니다.

**파일 경로**: `./.github/workflows/ci-cd.yml` (또는 `main.yml` 등)

**내용**:
```yaml
name: Java CI with Gradle and Docker

on:
  push:
    branches: [ "main", "master" ] # main 또는 master 브랜치에 push 발생 시 트리거
  pull_request:
    branches: [ "main", "master" ] # main 또는 master 브랜치로 pull request 발생 시 트리거

jobs:
  build-and-push-docker:
    runs-on: ubuntu-latest # 최신 Ubuntu 환경에서 실행

    steps:
    - name: Checkout code
      uses: actions/checkout@v4 # 리포지토리 코드 체크아웃

    - name: Set up JDK 17
      uses: actions/setup-java@v4
      with:
        java-version: '17' # Java 17 설정
        distribution: 'temurin' # Temurin OpenJDK 배포판 사용
        cache: 'gradle' # Gradle 캐시 활성화

    - name: Grant execute permission for gradlew
      run: chmod +x gradlew # gradlew 실행 권한 부여

    - name: Build with Gradle
      run: ./gradlew build # Gradle 빌드 실행 (테스트 포함)

    - name: Log in to Docker Hub
      uses: docker/login-action@v3
      with:
        username: ${{ secrets.DOCKER_USERNAME }} # GitHub Secrets에서 Docker Hub 사용자 이름 가져오기
        password: ${{ secrets.DOCKER_PASSWORD }} # GitHub Secrets에서 Docker Hub 비밀번호 가져오기

    - name: Build and push Docker image
      uses: docker/build-push-action@v5
      with:
        context: . # Dockerfile이 있는 컨텍스트 경로
        push: true # Docker Hub로 이미지 푸시
        tags: ${{ secrets.DOCKER_USERNAME }}/power-plant:latest # 이미지 태그 (예: your-dockerhub-username/power-plant:latest)
        # tags: ${{ secrets.DOCKER_USERNAME }}/power-plant:${{ github.sha }} # 커밋 SHA를 태그로 사용할 수도 있습니다.
```

### 3. GitHub Secrets 설정

GitHub 리포지토리에서 Docker Hub 인증 정보를 Secrets로 설정해야 합니다.

1.  GitHub 리포지토리로 이동합니다.
2.  `Settings` 탭을 클릭합니다.
3.  왼쪽 사이드바에서 `Security` -> `Secrets and variables` -> `Actions`를 클릭합니다.
4.  `New repository secret` 버튼을 클릭하여 다음 두 가지 Secret을 추가합니다:
    *   **`DOCKER_USERNAME`**: Docker Hub 사용자 이름
    *   **`DOCKER_PASSWORD`**: Docker Hub 비밀번호 (또는 Access Token)

---

## 🚀 다음 단계

위 단계를 완료한 후, `main` 또는 `master` 브랜치에 코드를 푸시하거나 Pull Request를 생성하면 GitHub Actions 워크플로우가 자동으로 실행되어 프로젝트를 빌드하고 Docker 이미지를 Docker Hub에 푸시할 것입니다.

---
