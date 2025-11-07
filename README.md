# 🚀 GitHub CI/CD Demo Project

이 프로젝트는 Spring Boot 애플리케이션을 위한 GitHub Actions 기반의 CI/CD(지속적 통합/지속적 배포) 파이프라인을 시연하기 위한 데모 프로젝트입니다.

---

## ✨ 주요 기능 및 목표

*   **Spring Boot 애플리케이션**: 간단한 웹 애플리케이션.
*   **GitHub Actions CI**: 코드가 푸시될 때마다 자동으로 빌드 및 테스트.
*   **Docker 이미지 빌드**: 애플리케이션을 Docker 이미지로 빌드하고 Docker Hub에 푸시.
*   **GitHub Actions CD**: Docker Hub에 푸시된 이미지를 원격 서버에 자동으로 배포.
*   **배포 상태 페이지**: 현재 배포된 애플리케이션의 빌드 시간 및 커밋 해시를 웹 페이지를 통해 확인.

---

## 🛠️ 기술 스택

*   **백엔드**: Java 21, Spring Boot 3.x, Gradle
*   **CI/CD**: GitHub Actions
*   **컨테이너**: Docker, Docker Hub
*   **배포**: SSH를 통한 원격 서버 배포

---

## 🚀 시작하기

### 1. 로컬에서 실행

프로젝트를 클론한 후, 다음 명령어로 로컬에서 애플리케이션을 실행할 수 있습니다.

```bash
./gradlew bootRun
```

애플리케이션은 기본적으로 `http://localhost:8080` 에서 실행됩니다.

### 2. CI/CD 파이프라인

이 프로젝트는 `main` 브랜치에 코드가 푸시될 때마다 GitHub Actions 워크플로우(`ci-cd.yml`)가 자동으로 실행됩니다.

**워크플로우 단계:**
1.  **Build and Push Docker Image**: Spring Boot 애플리케이션을 빌드하고 Docker 이미지를 생성하여 Docker Hub에 푸시합니다.
2.  **Deploy**: 원격 서버에 SSH로 접속하여 최신 Docker 이미지를 풀(pull)하고, 기존 컨테이너를 교체하여 새로운 버전을 배포합니다.

### 3. 배포 상태 확인

애플리케이션이 원격 서버에 배포된 후, 다음 URL을 통해 현재 배포된 버전의 빌드 정보를 확인할 수 있습니다.

`http://<서버 IP 주소>:49080/status.html`

---