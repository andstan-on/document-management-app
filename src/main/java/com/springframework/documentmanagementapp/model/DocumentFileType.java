package com.springframework.documentmanagementapp.model;

public enum DocumentFileType {
    PDF("application/pdf"), JPEG("image/png"), PNG("image/jpeg");

    private String contentType;

    DocumentFileType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

}
