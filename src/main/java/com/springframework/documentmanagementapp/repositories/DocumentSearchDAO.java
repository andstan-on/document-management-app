package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface DocumentSearchDAO {
    Page<Document> filterUsersDocuments(UUID userId, String searchedCriteria, Pageable pageable);

    Page<Document> filterApprovedDocuments(DocumentStatus documentStatus, String searchedCriteria, Pageable pageable);
    List<Document> filterDocuments(String searchedCriteria);
}
