package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.services.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

        if(! documentService.deleteById(documentId)){
            throw new NotFoundException();
        }

        return new ResponseEntity(HttpStatus.NO_CONTENT);

    }

    @PutMapping(DOCUMENT_PATH_ID)
    public ResponseEntity updateById(@PathVariable("documentId") UUID documentId, @RequestBody DocumentDTO document){
       if (documentService.updateDocumentById(documentId, document).isEmpty())
           throw new NotFoundException();

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(DOCUMENT_PATH)
    public ResponseEntity handlePost(@RequestBody DocumentDTO document){
        DocumentDTO savedDocument = documentService.saveNewDocument(document);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", DOCUMENT_PATH + "/" + savedDocument.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(DOCUMENT_PATH)
    public List<DocumentDTO> listDocuments(){
        return documentService.listDocuments();
    }

    @GetMapping(DOCUMENT_PATH_ID)
    public DocumentDTO getDocumentById(@PathVariable("documentId") UUID documentId) {

        log.debug("Get Document by ID - in controller");

        return documentService.getDocumentById(documentId).orElseThrow(NotFoundException::new);
    }
}
