package com.springframework.documentmanagementapp.repositories;

import com.springframework.documentmanagementapp.entities.Document;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class DocumentRepositoryTest {

    @Autowired
    DocumentRepository documentRepository;

    @Test
    void testSaveDocument() {
        Document savedDocument = documentRepository.save(Document.builder()
                .customerName("customer")
                .build());

        assertThat(savedDocument).isNotNull();
        assertThat(savedDocument.getId()).isNotNull();
    }

}