package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.Document;
import com.springframework.documentmanagementapp.services.DocumentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/document")
public class DocumentController {
    private final DocumentService documentService;

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
