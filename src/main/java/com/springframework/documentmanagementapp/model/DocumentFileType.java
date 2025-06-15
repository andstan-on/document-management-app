package com.springframework.documentmanagementapp.model;

public enum DocumentFileType {
    PDF("application/pdf"), JPEG("image/jpeg"), PNG("image/png");

    private String contentType;

    DocumentFileType(String contentType) {
        this.contentType = contentType;
    }

    public String getContentType() {
        return contentType;
    }

}
