package com.springframework.documentmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.documentmanagementapp.model.Document;
import com.springframework.documentmanagementapp.services.DocumentService;
import com.springframework.documentmanagementapp.services.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
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
    ArgumentCaptor<Document> documentArgumentCaptor;

    @BeforeEach
    void setUp() {
        documentServiceImpl = new DocumentServiceImpl();
    }

    @Test
    void deleteById() throws Exception {
        Document document = documentServiceImpl.listDocuments().get(0);

        mockMvc.perform(delete(DocumentController.DOCUMENT_PATH_ID, document.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(documentService).deleteById(uuidArgumentCaptor.capture());
        assertThat(document.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void updateById() throws Exception {
        Document document = documentServiceImpl.listDocuments().get(0);

        mockMvc.perform(put(DocumentController.DOCUMENT_PATH_ID, document.getId())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isNoContent());

        verify(documentService).updateDocumentById(any(UUID.class), any(Document.class));
    }

    @Test
    void handlePost() throws  Exception {
        Document document = documentServiceImpl.listDocuments().get(0);
        document.setId(null);

        given(documentService.saveNewDocument(any(Document.class))).willReturn(documentServiceImpl.listDocuments().get(1));

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
    void getDocumentById() throws Exception {
        Document testDocument = documentServiceImpl.listDocuments().get(0);

        given(documentService.getDocumentById(testDocument.getId())).willReturn(testDocument);

        mockMvc.perform(get(DocumentController.DOCUMENT_PATH_ID, testDocument.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testDocument.getId().toString())))
                .andExpect(jsonPath("$.vendorName", is(testDocument.getVendorName())));
    }
}