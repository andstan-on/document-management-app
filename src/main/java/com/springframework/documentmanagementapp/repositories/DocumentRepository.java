package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findByUserId(UUID userId);
    List<Document> findByApprovalStatus(DocumentStatus documentStatus);
}
