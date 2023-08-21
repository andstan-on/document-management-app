package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.entities.Token;
import com.springframework.documentmanagementapp.exception.EmailAlreadyExistsException;
import com.springframework.documentmanagementapp.exception.UsernameAlreadyExistsException;
import com.springframework.documentmanagementapp.model.*;
import com.springframework.documentmanagementapp.services.DocumentService;
import com.springframework.documentmanagementapp.services.TokenService;
import com.springframework.documentmanagementapp.services.UserRegistrationService;
import com.springframework.documentmanagementapp.services.UserService;
import com.springframework.documentmanagementapp.webutils.WebUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    private final TokenService tokenService;
    private final UserRegistrationService userRegistrationService;
    private final WebUtils webUtils;


    @GetMapping("/")
    public String viewHomePage(Model model) {
        return listUserDocuments(1, null, model);
    }

    @GetMapping(DOCUMENT_PATH)
    public String viewDocuments(Model model) {
        return listDocuments(1, null, model);
    }

    @GetMapping(DOCUMENT_PATH + "/page/{pageNumber}")
    public String listDocuments(@PathVariable Integer pageNumber,
                                @RequestParam(required = false) String searchedCriteria,
                                Model model){

        Page<DocumentDTO> documentPage = documentService.listDocumentsByApprovalStatus(DocumentStatus.APPROVED,searchedCriteria, pageNumber, 10);
        model.addAttribute("documentPage", documentPage);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", documentPage.getTotalPages());
        model.addAttribute("totalItems", documentPage.getTotalElements());

        return "document-list";
    }

    @GetMapping("/{pageNumber}")
    public String listUserDocuments(@PathVariable Integer pageNumber,
                                    @RequestParam(required = false) String searchedCriteria,
                                    Model model){

       Page<DocumentDTO> documentPage = documentService.listUserDocuments(null, searchedCriteria, pageNumber, 10);
        model.addAttribute("documentPage", documentPage);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", documentPage.getTotalPages());
        model.addAttribute("totalItems", documentPage.getTotalElements());


        return "index";
    }



    @GetMapping("/user/profile")
    public String getUserProfile(Model model){

        model.addAttribute("user", userService.getLoggedInUser().get());

        return "user-profile";
    }

    @GetMapping(DOCUMENT_PATH_ID)
    public String getDocumentFileById(@PathVariable("documentId") UUID documentId, HttpServletRequest request, Model model) {

        if(webUtils.accessNotApprovedDocuments(documentId) == false){
            return "no-access-document";
        }



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
        model.addAttribute("seeUpdateButtons", webUtils.accessDocumentChanges(documentId));
        model.addAttribute("seeChangeStatusButton", webUtils.accessDocumentStatusChanges());

        return "document-file";
    }

    @GetMapping("/upload")
    public String getUploadForm(Model model){

        model.addAttribute("documentDTOForm", new DocumentDTOForm());

        return "uploadDoc";
    }

    @PostMapping("/upload")
    public String uploadDoc(@Valid DocumentDTOForm documentDTOForm, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "uploadDoc";
        }
        try {
            DocumentDTO savedDocument = documentService.saveNewDocument(documentDTOForm);
            return "redirect:/";
        } catch (IllegalArgumentException ex) {
            model.addAttribute("errorMessage", "Doc type not supported");
            return "uploadDoc";
        }
    }

    @PostMapping(DOCUMENT_PATH_ID + "/delete")
    public String deleteDocument(@PathVariable("documentId") UUID documentId){

        if(webUtils.accessDocumentChanges(documentId) == false){
            return "no-access-document";
        }

        documentService.deleteById(documentId);
        return "redirect:/";
    }

    @GetMapping(DOCUMENT_PATH_ID + "/update/metadata")
    public String getUpdateDocMetadata(@PathVariable("documentId") UUID documentId, Model model){

        if(webUtils.accessDocumentChanges(documentId) == false){
            return "no-access-document";
        }

        DocumentDTO documentDTO = documentService.getDocumentMetadata(documentId).get();
        model.addAttribute("documentDTO", documentDTO);
        model.addAttribute("documentId", documentId);

        return "document-metadata-update";
    }

    @PostMapping(DOCUMENT_PATH_ID +"/update/metadata")
    public String postUpdateDocMetadata(@PathVariable("documentId") UUID documentId, @Valid DocumentDTO documentDTO, BindingResult result, Model model) {

        if(webUtils.accessDocumentChanges(documentId) == false){
            return "no-access-document";
        }

        if (result.hasErrors()) {
            return "document-metadata-update";
        }
        Optional<DocumentDTO> savedDocument = documentService.updateDocumentMetadata(documentId, documentDTO);
        return "redirect:" + DOCUMENT_PATH_ID;
    }

    @GetMapping(DOCUMENT_PATH_ID + "/update/file")
    public String getUpdateDocFile(@PathVariable("documentId") UUID documentId, Model model){

        if(webUtils.accessDocumentChanges(documentId) == false){
            return "no-access-document";
        }


        model.addAttribute("documentDTO", new DocumentDTO());
        model.addAttribute("documentId", documentId);

        return "document-file-update";
    }

    @PostMapping(DOCUMENT_PATH_ID +"/update/file")
    public String postUpdateDocFile(@PathVariable("documentId") UUID documentId, @Valid DocumentDTO documentDTO, BindingResult result, Model model) {

        if(webUtils.accessDocumentChanges(documentId) == false){
            return "no-access-document";
        }


        if (result.hasErrors()) {
            return "document-file-update";
        }
        Optional<DocumentDTO> savedDocument = documentService.updateDocumentFile(documentId, documentDTO);
        return "redirect:" + DOCUMENT_PATH_ID;
    }


    @GetMapping(DOCUMENT_PATH_ID + "/update/status")
    public String getUpdateDocStatus(@PathVariable("documentId") UUID documentId, Model model){

        if(webUtils.accessDocumentStatusChanges() == false){
            return "access-restricted";
        }

        model.addAttribute("documentId", documentId);

        return "document-status-update";
    }

    @PostMapping(DOCUMENT_PATH_ID +"/update/status")
    public String postUpdateDocStatus(@PathVariable("documentId") UUID documentId, @RequestPart("status") String status, BindingResult result, Model model) {

        if(webUtils.accessDocumentStatusChanges() == false){
            return "access-restricted";
        }

        if (result.hasErrors()) {
            return "document-status-update";
        }

        documentService.updateDocumentStatus(documentId, DocumentStatus.valueOf(status.toUpperCase()));

        return "redirect:" + DOCUMENT_PATH_ID;
    }

    @GetMapping("/admin/dashboard")
    public String getAdminPanelNoPage(Model model){
        return getAdminPanel(1, null, model);
    }

    @GetMapping("/admin/dashboard/{pageNumber}")
    public String getAdminPanel(@PathVariable Integer pageNumber,
                                @RequestParam(required = false) String searchedCriteria,
                                Model model){

        Page<DocumentDTO> documentPage = documentService.listDocumentsByApprovalStatus(DocumentStatus.PENDING,searchedCriteria, pageNumber, 10);
        model.addAttribute("documentPage", documentPage);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", documentPage.getTotalPages());
        model.addAttribute("totalItems", documentPage.getTotalElements());
        return "admin-panel-dashboard";
    }

    @GetMapping("/admin/users")
    public String listUsersAdminPanelNoPage(Model model){
        return listUsersAdminPanel(1, null, model);
    }

    @GetMapping("/admin/users/page/{pageNumber}")
    public String listUsersAdminPanel(@PathVariable Integer pageNumber,
                            @RequestParam(required = false) String searchedCriteria,
                            Model model){

        Page<UserDTO> userPage = userService.pageUsers(searchedCriteria, pageNumber, 10);
        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalItems", userPage.getTotalElements());
        return "admin-panel-users";
    }


    @GetMapping("/admin/create/user")
    public String getCreateUserForm(Model model){

        model.addAttribute("createUserDTO", new CreateUserDTO());

        return "admin-panel-create-user";
    }

    @PostMapping("/admin/create/user")
    public String postCreateUser(@Valid CreateUserDTO createUserDTO, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "admin-panel-create-user";
        }
        try {
            userRegistrationService.createUserInAdminPanel(createUserDTO);
            return "redirect:/admin/users/page/1";
        } catch (EmailAlreadyExistsException ex) {
            model.addAttribute("emailError", ex.getMessage());
            return "admin-panel-create-user";
        } catch (UsernameAlreadyExistsException ex) {
            model.addAttribute("usernameError", ex.getMessage());
            return "admin-panel-create-user";
        }
    }

    @GetMapping("/admin/user/{userId}/page/{pageNumber}")
    public String getUserInAdminPane(@PathVariable("userId") UUID userId,
                                     @PathVariable("pageNumber") Integer pageNumber,
                                     Model model){

        Page<DocumentDTO> documentPage = documentService.listUserDocuments(userId, null, pageNumber, 10);

        model.addAttribute("user", userService.getUserById(userId).get());
        model.addAttribute("documentPage", documentPage);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("totalPages", documentPage.getTotalPages());
        model.addAttribute("totalItems", documentPage.getTotalElements());

        return "admin-panel-user-profile";
    }

    @PostMapping("/admin/user/{userId}/delete")
    public String deleteUserAndRelatedDocs(@PathVariable("userId") UUID userId){

        List<DocumentDTO> documentDTOList = documentService.listDocuments(userId);

        for(DocumentDTO documentDTO : documentDTOList) {
            documentService.deleteById(documentDTO.getId());
        }

        tokenService.deleteUsersTokens(userId);
        userService.deleteUserById(userId);

        return "redirect:/admin/users";
    }

    @GetMapping("/admin/user/{userId}/update/role")
    public String getUpdateUserRoleForm(@PathVariable("userId") UUID userId, Model model){
        model.addAttribute("userId", userId);

        return "admin-panel-update-user-role";
    }

    @PostMapping("/admin/user/{userId}/update/role")
    public String adminPanelUpdateUserRole(@PathVariable("userId") UUID userId,@RequestPart("role") String role, BindingResult result, Model model){
        if (result.hasErrors()) {
            return "admin-panel-update-user-role";
        }

        userService.updateUserRole(userId, UserRole.valueOf(role.toUpperCase()));

        return "redirect:" + "/admin/user/{userId}/page/1";
    }

}
