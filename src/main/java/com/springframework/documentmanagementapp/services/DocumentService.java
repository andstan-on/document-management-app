package com.springframework.documentmanagementapp.services;

import com.springframework.documentmanagementapp.model.Document;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {

    List<Document> listDocuments();

    Optional<Document> getDocumentById(UUID id);


    Document saveNewDocument(Document document);

    void updateDocumentById(UUID documentId, Document document);

    void deleteById(UUID documentId);

}
