package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.*;
import com.springframework.documentmanagementapp.services.DocumentService;
import com.springframework.documentmanagementapp.services.UserService;
import com.springframework.documentmanagementapp.webutils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class DocumentControllerView {

    private static final Logger logger = LoggerFactory.getLogger(DocumentControllerView.class);

    public static final String DOCUMENT_PATH = "/document";
    public static final String DOCUMENT_PATH_ID = DOCUMENT_PATH + "/{documentId}";

    private final DocumentService documentService;
    private final UserService userService;


    @GetMapping(DOCUMENT_PATH)
    public String listDocuments( Model model){

        List<DocumentDTO> documents = documentService.listDocuments();
        model.addAttribute("documents", documents);
        return "document-list";
    }

    @GetMapping("/")
    public String listUserDocuments( Model model){


        List<DocumentDTO> documents = documentService.listUserDocuments();
        model.addAttribute("documents", documents);

        return "index";
    }

    @GetMapping("/user/profile")
    public String getUserProfile(Model model){

        model.addAttribute("user", userService.getLoggedInUser().get());

        return "user-profile";
    }

    @GetMapping(DOCUMENT_PATH_ID)
    public String getDocumentFileById(@PathVariable("documentId") UUID documentId, HttpServletRequest request, Model model) {

        // Load file as Resource
        Resource resource = documentService.getDocumentFile(documentId);

        DocumentDTO documentDTO = documentService.getDocumentMetadata(documentId).get();


        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }

        //get file type
        // Extract the extension from the file name
        String fileType = "type";
        int index = resource.getFilename().lastIndexOf('.');
        if (index > 0) {
            fileType = resource.getFilename().substring(index + 1);
        }

        model.addAttribute("resource",resource);
        model.addAttribute("documentId",documentId);
        model.addAttribute("fileType", fileType);
        model.addAttribute("document",documentDTO);

        return "document-file";
    }

    @GetMapping("/upload")
    public String getUploadForm(Model model){

        model.addAttribute("documentDTO", new DocumentDTO());

        return "uploadDoc";
    }

    @PostMapping("/upload")
    public String uploadDoc(@Valid DocumentDTO documentDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "uploadDoc";
        }
        try {
            DocumentDTO savedDocument = documentService.saveNewDocument(documentDTO);
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", "Doc type not supported");
            return "uploadDoc";
        }
    }

    @PostMapping(DOCUMENT_PATH_ID + "/delete")
    public String deleteDocument(@PathVariable("documentId") UUID documentId){
        documentService.deleteById(documentId);
        return "redirect:/";
    }

    @GetMapping(DOCUMENT_PATH_ID + "/update/metadata")
    public String getUpdateDocMetadata(@PathVariable("documentId") UUID documentId, Model model){

        DocumentDTO documentDTO = documentService.getDocumentMetadata(documentId).get();
        model.addAttribute("documentDTO", documentDTO);
        model.addAttribute("documentId", documentId);

        return "document-metadata-update";
    }

    @PostMapping(DOCUMENT_PATH_ID +"/update/metadata")
    public String postUpdateDocMetadata(@PathVariable("documentId") UUID documentId, @Valid DocumentDTO documentDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "document-metadata-update";
        }
        Optional<DocumentDTO> savedDocument = documentService.updateDocumentMetadata(documentId, documentDTO);
        return "redirect:" + DOCUMENT_PATH_ID;
    }

    @GetMapping(DOCUMENT_PATH_ID + "/update/file")
    public String getUpdateDocFile(@PathVariable("documentId") UUID documentId, Model model){

        model.addAttribute("documentDTO", new DocumentDTO());
        model.addAttribute("documentId", documentId);

        return "document-file-update";
    }

    @PostMapping(DOCUMENT_PATH_ID +"/update/file")
    public String postUpdateDocFile(@PathVariable("documentId") UUID documentId, @Valid DocumentDTO documentDTO, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "document-file-update";
        }
        Optional<DocumentDTO> savedDocument = documentService.updateDocumentFile(documentId, documentDTO);
        return "redirect:" + DOCUMENT_PATH_ID;
    }


    @GetMapping(DOCUMENT_PATH_ID + "/update/status")
    public String getUpdateDocStatus(@PathVariable("documentId") UUID documentId, Model model){

        model.addAttribute("documentId", documentId);

        return "document-status-update";
    }

    @PostMapping(DOCUMENT_PATH_ID +"/update/status")
    public String postUpdateDocStatus(@PathVariable("documentId") UUID documentId, @RequestPart("status") String status, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "document-status-update";
        }

        documentService.updateDocumentStatus(documentId, DocumentStatus.valueOf(status.toUpperCase()));

        return "redirect:" + DOCUMENT_PATH_ID;
    }

    @GetMapping("/admin")
    public String getAdminPanel(Model model){

        List<DocumentDTO> pendingDocuments = documentService.listDocumentsByApprovalStatus(DocumentStatus.PENDING);

        model.addAttribute("pendingDocuments", pendingDocuments);
        return "admin-panel";
    }


}
