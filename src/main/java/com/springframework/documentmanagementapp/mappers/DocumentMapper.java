package com.springframework.documentmanagementapp.mappers;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocumentMapper {

    Document documentDtoToDocument(DocumentDTO dto);

    DocumentDTO documentToDocumentDto(Document document);

}
