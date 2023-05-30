package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.Document;
import com.springframework.documentmanagementapp.services.DocumentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DocumentController {

    public static final String DOCUMENT_PATH = "/api/v1/document";
    public static final String DOCUMENT_PATH_ID = DOCUMENT_PATH + "/{documentId}";

    private final DocumentService documentService;

    @DeleteMapping(DOCUMENT_PATH_ID)
    public ResponseEntity deleteById(@PathVariable("documentId") UUID documentId){
        documentService.deleteById(documentId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PutMapping(DOCUMENT_PATH_ID)
    public ResponseEntity updateById(@PathVariable("documentId") UUID documentId, @RequestBody Document document){
        documentService.updateDocumentById(documentId, document);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(DOCUMENT_PATH)
    public ResponseEntity handlePost(@RequestBody Document document){
        Document savedDocument = documentService.saveNewDocument(document);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/document/" + savedDocument.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(DOCUMENT_PATH)
    public List<Document> listDocuments(){
        return documentService.listDocuments();
    }

    @GetMapping(DOCUMENT_PATH_ID)
    public Document getDocumentById(@PathVariable("documentId") UUID documentId) {

        log.debug("Get Document by ID - in controller");

        return documentService.getDocumentById(documentId);
    }
}
