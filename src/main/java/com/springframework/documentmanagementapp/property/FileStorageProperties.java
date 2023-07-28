package com.springframework.documentmanagementapp.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String testUploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public String getTestUploadDir() {
        return testUploadDir;
    }

    public void setTestUploadDir(String testUploadDir) {
        this.testUploadDir = testUploadDir;
    }
}
