package com.springframework.documentmanagementapp.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Builder
@Data
public class CompleteDocumentDTO extends BaseDocumentDTO {
    private UUID id;
    private MultipartFile docFile;
}
