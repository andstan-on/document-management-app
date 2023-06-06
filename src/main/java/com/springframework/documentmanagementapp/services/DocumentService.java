package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.DocumentDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {

    List<DocumentDTO> listDocuments();

    Optional<DocumentDTO> getDocumentById(UUID id);


    DocumentDTO saveNewDocument(DocumentDTO document);

    Optional<DocumentDTO> updateDocumentById(UUID documentId, DocumentDTO document);

    Boolean deleteById(UUID documentId);

}
