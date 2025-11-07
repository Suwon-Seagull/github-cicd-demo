# CI/CD 실습 기록

이 문서는 `ci_cd_practice_plan.md`에 따라 진행된 실습의 완료 기록을 관리합니다.

---

*   **[완료] 1단계: Dockerfile 생성** (2025-11-07)
    *   애플리케이션을 Docker 이미지로 만들기 위한 설계도를 작성했습니다.
    *   (수정) 프로젝트의 JDK 버전에 맞춰 Java 21로 수정하고, JAR 파일명을 수정했습니다.
*   **[완료] 2단계: GitHub Actions 워크플로우 파일 생성** (2025-11-07)
    *   코드가 푸시될 때 자동으로 Docker 이미지를 빌드하고 푸시하는 CI/CD 파이프라인을 정의했습니다.
    *   (수정) JDK 버전을 21로, Docker 이미지 태그를 프로젝트에 맞게 수정했습니다.

---

## 📚 생성/수정된 주요 문서 및 파일

*   **`GIT_CONVENTION.md`**: Git 커밋 메시지 컨벤션 및 브랜치 전략 (예시 포함)
*   **`PRACTICE_HISTORY.md`**: CI/CD 실습 진행 상황 및 완료 기록
*   **`TROUBLESHOOTING_LOG.md`**: CI/CD 구축 중 발생한 주요 이슈 및 해결 과정 상세 기록
*   **`ci_cd_practice_plan.md`**: CI/CD 실습 계획 (기존 파일)
*   **`Dockerfile`**: Spring Boot 애플리케이션을 Docker 이미지로 빌드하기 위한 설정 파일 (수정됨)
*   **`.github/workflows/ci-cd.yml`**: GitHub Actions 워크플로우 정의 파일 (CI/CD 파이프라인, 수정됨)
*   **`build.gradle`**: Gradle 빌드 설정 파일 (Actuator 및 빌드 정보 생성 설정 추가, 수정됨)
*   **`src/main/java/.../StatusController.java`**: 애플리케이션 상태 정보를 제공하는 Spring Boot 컨트롤러 (새로 생성 및 수정됨)
*   **`src/main/resources/static/status.html`**: 배포 상태를 시각적으로 보여주는 웹 페이지 (새로 생성 및 수정됨)

---

## ✨ 주요 학습 내용 및 개념

### 1. Git 및 GitHub 활용
*   **Git 커밋 컨벤션**: `type`, `scope`, `subject`, `body`, `footer`를 활용한 체계적인 커밋 메시지 작성법.
*   **Git CLI 명령어**: `git add`, `git commit`, `git push`, `git config`, `git reset --soft`, `git commit --amend`, `git push --force` 기본 사용법.
*   **GitHub 인증**: CLI에서 Personal Access Token (PAT)을 사용하여 GitHub에 인증하는 방법.
*   **SSH 키 관리**: `ssh-keygen`을 이용한 SSH 키 쌍 생성, 공개 키/개인 키의 역할 이해, `.ppk` 파일을 OpenSSH 형식으로 변환하는 방법.

### 2. CI/CD 파이프라인 구축 (GitHub Actions)
*   **워크플로우 정의**: `.github/workflows/*.yml` 파일을 통한 GitHub Actions 워크플로우 구성.
*   **트리거**: `push`, `pull_request` 이벤트에 따른 워크플로우 자동 실행 설정.
*   **작업(Jobs)**: `build-and-push-docker` (CI) 및 `deploy` (CD) 작업 정의 및 의존성(`needs`) 설정.
*   **단계(Steps)**: `actions/checkout`, `setup-java`, `docker/login-action`, `docker/build-push-action`, `appleboy/ssh-action` 등 다양한 액션 활용법.
*   **Secrets 관리**: `DOCKER_USERNAME`, `DOCKER_PASSWORD`, `SERVER_HOST`, `SERVER_USERNAME`, `SSH_PRIVATE_KEY` 등 민감 정보를 GitHub Secrets에 안전하게 저장하고 워크플로우에서 활용하는 방법.
*   **빌드 인자 전달**: GitHub Actions 환경 변수(`github.sha`)를 Docker 빌드 인자(`build-args`)로 전달하고, 이를 다시 Gradle 프로젝트 속성(`-P`)으로 전달하여 빌드 정보에 포함시키는 과정.

### 3. Docker 및 컨테이너 기술
*   **`Dockerfile` 작성**: `FROM`, `WORKDIR`, `COPY`, `RUN`, `EXPOSE`, `ENTRYPOINT` 등 기본 명령어 이해 및 활용.
*   **Docker 이미지 태그**: `21-slim`, `eclipse-temurin:21-jdk` 등 안정적인 이미지 태그 선택 및 `latest` 태그 사용 시 주의점.
*   **Docker 빌드 트러블슈팅**: `gradlew` 실행 권한 문제, `gradle-wrapper.properties` 누락 문제 해결.
*   **컨테이너 배포**: `docker pull`, `docker stop`, `docker rm`, `docker run` 명령어를 이용한 컨테이너 배포 및 포트 매핑(`-p 49080:8080`).

### 4. Spring Boot 애플리케이션
*   **Actuator `build-info`**: Spring Boot Actuator를 활용하여 애플리케이션 빌드 정보를 자동으로 생성하고 노출하는 방법.
*   **`StatusController`**: `BuildProperties` 객체를 주입받아 빌드 시간 및 커밋 해시를 API로 제공하는 컨트롤러 구현.
*   **정적 웹 페이지 제공**: `src/main/resources/static` 디렉토리를 활용하여 HTML, CSS, JavaScript 파일을 제공하는 방법.
