package com.example.cicd.github_cicd_demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.InputStream;
import java.util.Properties;
import java.util.Map;
import java.util.HashMap;

/**
 * 애플리케이션의 빌드 및 배포 상태를 보여주는 API 컨트롤러
 */
@RestController
@RequestMapping("/api")
public class StatusController {

    /**
     * /api/status 경로로 요청이 오면, 빌드 정보를 담은 JSON을 반환합니다.
     * @return 빌드 시간과 커밋 해시 정보를 담은 Map 객체
     */
    @GetMapping("/status")
    public Map<String, String> getStatus() {
        Map<String, String> statusInfo = new HashMap<>();
        try {
            Properties properties = new Properties();
            // Classpath에서 version.properties 파일을 읽어옵니다.
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("version.properties");

            if (inputStream != null) {
                properties.load(inputStream);
                statusInfo.put("buildTime", properties.getProperty("build.time", "N/A"));
                statusInfo.put("commitHash", properties.getProperty("build.commit", "N/A"));
                inputStream.close();
            } else {
                statusInfo.put("error", "version.properties not found");
            }
        } catch (Exception e) {
            statusInfo.put("error", "Failed to read version.properties");
            statusInfo.put("errorMessage", e.getMessage());
        }
        return statusInfo;
    }
}
