package com.springframework.documentmanagementapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.model.DocumentDTOForm;
import com.springframework.documentmanagementapp.services.DocumentService;
import com.springframework.documentmanagementapp.services.DocumentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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
@AutoConfigureMockMvc(addFilters = false)
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
        DocumentDTO document = documentServiceImpl.listDocuments(null).get(0);

        given(documentService.deleteById(any())).willReturn(true);

        mockMvc.perform(delete(DocumentController.REST_DOCUMENT_PATH_ID, document.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(documentService).deleteById(uuidArgumentCaptor.capture());
        assertThat(document.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }


    @Test
    void handlePost() throws  Exception {
        DocumentDTO document = documentServiceImpl.listDocuments(null).get(0);
        document.setId(null);

        given(documentService.saveNewDocument(any(DocumentDTOForm.class))).willReturn(documentServiceImpl.listDocuments(null).get(1));

        mockMvc.perform(post(DocumentController.REST_DOCUMENT_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(document)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void listDocuments() throws Exception {
        given(documentService.listDocuments(null)).willReturn(documentServiceImpl.listDocuments(null));

        mockMvc.perform(get(DocumentController.REST_DOCUMENT_PATH)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getDocumentByIdNotFound() throws Exception {

        given(documentService.getDocumentMetadata(any(UUID.class))).willReturn(Optional.empty());

        mockMvc.perform(get(DocumentController.REST_DOCUMENT_PATH_ID + "/metadata", UUID.randomUUID()))
                .andExpect(status().isNotFound());

    }

    @Test
    void getDocumentById() throws Exception {
        DocumentDTO testDocument = documentServiceImpl.listDocuments(null).get(0);

        given(documentService.getDocumentMetadata(testDocument.getId())).willReturn(Optional.of(testDocument));

        mockMvc.perform(get(DocumentController.REST_DOCUMENT_PATH_ID + "/metadata", testDocument.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(testDocument.getId().toString())))
                .andExpect(jsonPath("$.vendorName", is(testDocument.getVendorName())));
    }
}