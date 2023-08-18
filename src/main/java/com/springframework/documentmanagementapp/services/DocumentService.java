package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {

    Page<DocumentDTO> listUserDocuments(UUID userId, String searchedCriteria, Integer pageNumber, Integer pageSize);

    Page<DocumentDTO> listDocumentsByApprovalStatus(DocumentStatus documentStatus, String searchedCriteria, Integer pageNumber, Integer pageSize);

    List<DocumentDTO> filterDocuments(String searchedCriteria);

    List<DocumentDTO> listDocuments(UUID userId);

    Optional<DocumentDTO> getDocumentMetadata(UUID id);

    Resource getDocumentFile(UUID id);

    DocumentDTO saveNewDocument(DocumentDTO document);

    Optional<DocumentDTO> updateDocumentMetadata(UUID documentId, DocumentDTO document);

    Optional<DocumentDTO> updateDocumentFile(UUID documentId, DocumentDTO document);

    Optional<DocumentDTO> updateDocumentStatus(UUID documentId, DocumentStatus documentStatus);

    Boolean deleteById(UUID documentId);


}
