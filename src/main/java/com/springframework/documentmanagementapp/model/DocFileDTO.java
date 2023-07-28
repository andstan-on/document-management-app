package com.springframework.documentmanagementapp.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Data
@Builder
public class DocFileDTO extends BaseDocumentDTO {
    private UUID id;
    private MultipartFile docFile;
    private String fileName;
    private DocumentFileType fileType;
    private String filePath;


}
