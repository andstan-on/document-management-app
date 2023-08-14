package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.entities.Document;
import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.exception.NotFoundException;
import com.springframework.documentmanagementapp.mappers.DocumentMapper;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.UserRole;
import com.springframework.documentmanagementapp.repositories.DocumentRepository;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.security.Principal;
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
    UserRepository userRepository;


    @Autowired
    DocumentMapper documentMapper;

    MockMultipartFile file
            = new MockMultipartFile(
            "file",
            "hello.pdf",
            MediaType.APPLICATION_PDF_VALUE,
            "Hello, World!".getBytes()
    );

    @Test
    void testDeleteByIdNotFound() {
        assertThrows(NotFoundException.class, () -> {
            documentController.deleteById(UUID.randomUUID());
        });
    }

  /*  @Rollback
    @Transactional
    @Test
    void testDeleteById() {
        Document document = documentRepository.findAll().get(0);

        ResponseEntity responseEntity = documentController.deleteById(document.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));

        assertThat(documentRepository.findById(document.getId()).isEmpty());
    }
*/


    @Rollback
    @Transactional
    @Test
    void saveNewDocumentTest() {

        DocumentDTO documentDTO = DocumentDTO.builder()
                .docFile(file)
                .vendorName("vendor11")
                .user(userRepository.findAll().get(0))
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

        HttpServletRequest request = new MockHttpServletRequest();

        assertThrows(NotFoundException.class, () -> {
            documentController.getDocumentMetadataById(UUID.randomUUID());
        });
    }

    @Test
    void testGetById() {
        Document document = documentRepository.findAll().get(0);

        HttpServletRequest request = new MockHttpServletRequest();

        DocumentDTO documentDTO = documentController.getDocumentMetadataById(document.getId());

        assertThat(documentDTO.getCustomerName()).isEqualTo("name2");
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