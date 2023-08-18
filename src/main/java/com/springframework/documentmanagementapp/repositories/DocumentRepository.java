package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID>, DocumentSearchDAO {

    Page<Document> findByUserId(UUID userId, Pageable pageable);
    List<Document> findByUserId(UUID userId);
    Page<Document> findByApprovalStatus(DocumentStatus documentStatus, Pageable pageable);
    Page<Document> filterUsersDocuments(UUID userId, String searchedCriteria, Pageable pageable);
    Page<Document> filterApprovedDocuments(DocumentStatus documentStatus, String searchedCriteria, Pageable pageable);
    List<Document> filterDocuments(String searchedCriteria);
}
