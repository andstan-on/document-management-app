package com.springframework.documentmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.services.DocumentService;
import com.springframework.documentmanagementapp.services.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DocumentController.class)
class DocumentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    DocumentService documentService;

    DocumentServiceImpl documentServiceImpl;

    @Captor
    ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Captor
    ArgumentCaptor<DocumentDTO> documentArgumentCaptor;

    @BeforeEach
    void setUp() {
        documentServiceImpl = new DocumentServiceImpl();
    }

    @Test
    void deleteById() throws Exception {
        DocumentDTO document = documentServiceImpl.listDocuments().get(0);

        given(documentService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(DocumentController.DOCUMENT_PATH_ID, document.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(documentService).deleteById(uuidArgumentCaptor.capture());
        assertThat(document.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updateById() throws Exception {
        DocumentDTO document = documentServiceImpl.listDocuments().get(0);

        given(documentService.updateDocumentById(any(), any())).willReturn(Optional.of(document));

        mockMvc.perform(put(DocumentController.DOCUMENT_PATH_ID, document.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isNoContent());

        verify(documentService).updateDocumentById(any(UUID.class), any(DocumentDTO.class));
    }

    @Test
    void handlePost() throws  Exception {
        DocumentDTO document = documentServiceImpl.listDocuments().get(0);
        document.setId(null);

        given(documentService.saveNewDocument(any(DocumentDTO.class))).willReturn(documentServiceImpl.listDocuments().get(1));

        mockMvc.perform(post(DocumentController.DOCUMENT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listDocuments() throws Exception {
        given(documentService.listDocuments()).willReturn(documentServiceImpl.listDocuments());

        mockMvc.perform(get(DocumentController.DOCUMENT_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getDocumentByIdNotFound() throws Exception {

        given(documentService.getDocumentById(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(DocumentController.DOCUMENT_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());

    }

    @Test
    void getDocumentById() throws Exception {
        DocumentDTO testDocument = documentServiceImpl.listDocuments().get(0);

        given(documentService.getDocumentById(testDocument.getId())).willReturn(Optional.of(testDocument));

        mockMvc.perform(get(DocumentController.DOCUMENT_PATH_ID, testDocument.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testDocument.getId().toString())))
                .andExpect(jsonPath("$.vendorName", is(testDocument.getVendorName())));
    }
}