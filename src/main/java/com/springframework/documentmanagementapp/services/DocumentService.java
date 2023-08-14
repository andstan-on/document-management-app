package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import org.springframework.core.io.Resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {

    List<DocumentDTO> listUserDocuments();

    List<DocumentDTO> listDocumentsByApprovalStatus(DocumentStatus documentStatus);

    List<DocumentDTO> listDocuments();

    Optional<DocumentDTO> getDocumentMetadata(UUID id);

    Resource getDocumentFile(UUID id);

    DocumentDTO saveNewDocument(DocumentDTO document);

    Optional<DocumentDTO> updateDocumentMetadata(UUID documentId, DocumentDTO document);

    Optional<DocumentDTO> updateDocumentFile(UUID documentId, DocumentDTO document);

    Optional<DocumentDTO> updateDocumentStatus(UUID documentId, DocumentStatus documentStatus);

    Boolean deleteById(UUID documentId);


}
