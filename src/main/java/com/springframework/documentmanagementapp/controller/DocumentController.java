package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.Document;
import com.springframework.documentmanagementapp.services.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {

    @Autowired
    private  DocumentService documentService;

    @DeleteMapping("{documentId}")
    public ResponseEntity deleteById(@PathVariable("documentId") UUID documentId){
        documentService.deleteById(documentId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PutMapping("{documentId}")
    public ResponseEntity updateById(@PathVariable("documentId") UUID documentId, @RequestBody Document document){
        documentService.updateDocumentById(documentId, document);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity handlePost(@RequestBody Document document){
        Document savedDocument = documentService.saveNewDocument(document);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", "/api/v1/document/" + savedDocument.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Document> listDocuments(){
        return documentService.listDocuments();
    }

    @RequestMapping(value = "{documentId}", method = RequestMethod.GET)
    public Document getDocumentById(@PathVariable("documentId") UUID documentId) {

        log.debug("Get Document by ID - in controller");

        return documentService.getDocumentById(documentId);
    }
}
