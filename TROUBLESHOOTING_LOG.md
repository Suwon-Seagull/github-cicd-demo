# CI/CD 파이프라인 구축 트러블슈팅 로그

이 문서는 CI/CD 파이프라인을 구축하면서 발생했던 주요 이슈와 해결 과정을 기록합니다.

---

### 1. Git 인증 실패 (Authentication Failed)

-   **문제 현상:**
    -   `git push` 시 `fatal: Authentication failed` 오류 발생.
    -   `git: 'credential-ghp_...' is not a git command` 라는 추가 오류 발생.

-   **원인 분석:**
    -   `git config credential.helper` 설정에 인증 방식(`cache` 등) 대신 Personal Access Token (PAT) 값 자체를 잘못 설정함.
    -   이로 인해 Git이 토큰 값을 명령어로 인식하고 실행하려다 오류 발생.

-   **해결 조치:**
    1.  `git config --global --unset credential.helper` 명령어로 잘못된 설정을 삭제.
    2.  `git config --global credential.helper 'cache --timeout=3600'` 명령어로 올바른 인증 캐시 방식 설정.
    3.  `git push` 시 비밀번호를 묻는 프롬프트에 PAT 값을 입력하여 인증 성공.

---

### 2. Docker 이미지 빌드 실패 (`image not found`)

-   **문제 현상:**
    -   GitHub Actions의 Docker 빌드 단계에서 `openjdk:21-jdk-slim: not found` 또는 `openjdk:21-slim: not found` 오류 발생.

-   **원인 분석:**
    -   `Dockerfile`의 `FROM`에 명시된 Docker 이미지 태그가 Docker Hub에 존재하지 않음.
    -   워크플로우의 빌드 환경(`eclipse-temurin`)과 `Dockerfile`의 실행 환경(`openjdk`)이 서로 다른 배포판을 사용하여 발생한 혼선.

-   **해결 조치:**
    -   빌드/실행 환경을 통일하기 위해 `Dockerfile`의 기본 이미지를 `FROM eclipse-temurin:21-jdk` 으로 변경하여 해결.

---

### 3. Dockerfile 내부 Gradle 빌드 실패 (`exit code: 1`)

-   **문제 현상:**
    -   `RUN ./gradlew bootJar` 단계에서 `exit code: 1` 오류와 함께 빌드 실패. 로컬에서는 성공했으나 CI 환경의 Docker 빌드에서만 발생.

-   **원인 분석 및 해결 조치:**

    1.  **실행 권한 부재:**
        -   **원인:** `Dockerfile`의 `COPY` 명령은 파일의 실행 권한을 보존하지 않음. 따라서 Docker 컨테이너 안의 `gradlew` 스크립트를 실행할 권한이 없었음.
        -   **해결:** `RUN chmod +x ./gradlew` 명령어를 추가하여 실행 권한을 부여.

    2.  **Gradle Wrapper 속성 파일 누락:**
        -   **원인:** `gradle-wrapper.properties` 파일이 `Dockerfile`에서 누락됨. 이 파일은 Gradle 버전을 지정하는 등 Wrapper 실행에 필수적임.
        -   **해결:** `COPY gradle/wrapper/gradle-wrapper.jar` 라인을 `COPY gradle ./gradle` 로 변경하여, `gradle` 디렉토리 전체를 복사하도록 수정.

---

### 4. 상태 페이지 빌드 정보 누락 (`version.properties not found`)

-   **문제 현상:**
    -   배포된 애플리케이션의 `/api/status` 엔드포인트에서 `version.properties not found` 오류 발생.

-   **원인 분석:**
    -   이전 방식(`createVersionProperties` 태스크)으로는 Docker 빌드 환경에서 `version.properties` 파일이 JAR에 제대로 포함되지 않음.
    -   Docker 빌드 환경에는 `.git` 디렉토리가 없어 Git 커밋 해시를 자동으로 가져올 수 없음.

-   **해결 조치:**
    1.  **`build.gradle` 수정:** `spring-boot-starter-actuator` 의존성 추가 및 `springBoot { buildInfo() }` 설정.
    2.  **`StatusController.java` 수정:** `BuildProperties` 객체를 주입받아 빌드 정보를 읽도록 변경.
    3.  **`ci-cd.yml` 수정:** GitHub Actions의 `github.sha`를 Docker 빌드 인자(`COMMIT_ID`)로 전달.
    4.  **`Dockerfile` 수정:** `ARG COMMIT_ID`를 선언하고 `gradlew bootJar -PcommitId=$COMMIT_ID`로 Gradle에 전달.
    5.  **`build.gradle` 재수정:** `buildInfo` 설정에서 `project.findProperty('commitId')`를 사용하여 전달받은 커밋 해시를 `build-info.properties`에 포함.

