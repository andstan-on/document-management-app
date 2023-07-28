package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.DocumentDTO;
import com.springframework.documentmanagementapp.services.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

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

    @PutMapping(DOCUMENT_PATH_ID + "/metadata")
    public ResponseEntity updateMetadataById(@PathVariable("documentId") UUID documentId,  @RequestBody DocumentDTO document){
       if (documentService.updateDocumentMetadata(documentId, document).isEmpty())
           throw new NotFoundException();

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @PostMapping(DOCUMENT_PATH)
    public ResponseEntity handlePost(@ModelAttribute DocumentDTO document){

        DocumentDTO savedDocument = documentService.saveNewDocument(document);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", DOCUMENT_PATH + "/" + savedDocument.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @GetMapping(DOCUMENT_PATH)
    public List<DocumentDTO> listDocuments(Model model){

        List<DocumentDTO> documents = documentService.listDocuments();
        model.addAttribute("documents", documents);
        return documents;
    }


    @GetMapping(DOCUMENT_PATH_ID +"/metadata")
    public DocumentDTO getDocumentMetadataById(@PathVariable("documentId") UUID documentId) {

        log.debug("Get Document by ID - in controller");

        return documentService.getDocumentMetadata(documentId).orElseThrow(NotFoundException::new);
    }


    @GetMapping(DOCUMENT_PATH_ID)
    public ResponseEntity<Resource> getDocumentFileById(@PathVariable("documentId") UUID documentId, HttpServletRequest request) {

        // Load file as Resource
        Resource resource = documentService.getDocumentFile(documentId);


        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PutMapping(DOCUMENT_PATH_ID)
    public ResponseEntity updateDocumentFile(@PathVariable("documentId") UUID documentId, @ModelAttribute DocumentDTO document) {
        if (documentService.updateDocumentFile(documentId, document).isEmpty())
            throw new NotFoundException();

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
