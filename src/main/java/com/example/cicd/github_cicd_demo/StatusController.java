package com.example.cicd.github_cicd_demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.HashMap;

/**
 * 애플리케이션의 빌드 및 배포 상태를 보여주는 API 컨트롤러
 */
@RestController
@RequestMapping("/api")
public class StatusController {

    private final BuildProperties buildProperties;

    // Spring이 자동으로 BuildProperties 객체를 주입해줍니다.
    @Autowired
    public StatusController(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    /**
     * /api/status 경로로 요청이 오면, 빌드 정보를 담은 JSON을 반환합니다.
     * @return 빌드 시간과 커밋 해시 정보를 담은 Map 객체
     */
    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> statusInfo = new HashMap<>();
        
        // buildProperties에서 빌드 시간과 커밋 정보를 가져옵니다.
        String buildTime = DateTimeFormatter.ISO_INSTANT.format(buildProperties.getTime());
        String commitHash = buildProperties.get("git.commit.id");

        statusInfo.put("buildTime", buildTime);
        statusInfo.put("commitHash", commitHash != null ? commitHash : "N/A");
        
        return statusInfo;
    }
}
