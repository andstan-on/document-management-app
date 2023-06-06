package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.mappers.DocumentMapper;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.repositories.DocumentRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DocumentControllerIT {

    @Autowired
    DocumentController documentController;

    @Autowired
    DocumentRepository documentRepository;

    @Autowired
    DocumentMapper documentMapper;

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            documentController.deleteById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        Document document = documentRepository.findAll().get(0);

        ResponseEntity responseEntity = documentController.deleteById(document.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(documentRepository.findById(document.getId()).isEmpty());
    }

    @Test
    void testUpdateNotFound() {
        assertThrows(NotFoundException.class, () -> {
            documentController.updateById(UUID.randomUUID(), DocumentDTO.builder().build());
        });
    }

    @Rollback
    @Transactional
    @Test
    void updateExistingDocument() {
        Document document = documentRepository.findAll().get(0);

        DocumentDTO documentDTO = documentMapper.documentToDocumentDto(document);
        documentDTO.setId(null);
        final String vendorName = "Updated";
        documentDTO.setVendorName(vendorName);

        ResponseEntity responseEntity =  documentController.updateById(document.getId(), documentDTO);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        Document updatedDocument = documentRepository.findById(document.getId()).get();
        assertThat(updatedDocument.getVendorName()).isEqualTo(vendorName);
    }

    @Rollback
    @Transactional
    @Test
    void saveNewDocumentTest() {
        DocumentDTO documentDTO = DocumentDTO.builder()
                .vendorName("vendor11")
                .build();

        ResponseEntity responseEntity = documentController.handlePost(documentDTO);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] location = responseEntity.getHeaders().getLocation().getPath().split("/");
        UUID savedUUID = UUID.fromString(location[4]);

        Document document = documentRepository.findById(savedUUID).get();
        assertThat(document).isNotNull();

    }

    @Rollback
    @Transactional
    @Test
    void testGetByIdNotFound() {

        documentRepository.deleteAll();

        assertThrows(NotFoundException.class, () -> {
            documentController.getDocumentById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Document document = documentRepository.findAll().get(0);

        DocumentDTO documentDTO = documentController.getDocumentById(document.getId());

        assertThat(documentDTO).isNotNull();
    }

    @Test
    void testListDocuments() {
        List<DocumentDTO> dtos = documentController.listDocuments();

        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void testEmptyList() {

        documentRepository.deleteAll();

        List<DocumentDTO> dtos = documentController.listDocuments();

        assertThat(dtos.size()).isEqualTo(0);
    }

}