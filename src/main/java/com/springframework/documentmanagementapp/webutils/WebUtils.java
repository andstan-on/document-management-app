package com.springframework.documentmanagementapp.webutils;

import com.springframework.documentmanagementapp.entities.User;
import com.springframework.documentmanagementapp.model.DocumentStatus;
import com.springframework.documentmanagementapp.model.UserRole;
import com.springframework.documentmanagementapp.services.DocumentService;
import com.springframework.documentmanagementapp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class WebUtils {

    private final DocumentService documentService;
    private final UserService userService;

    public static User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        return user;
    }

    public boolean accessNotApprovedDocuments(UUID documentId){

        DocumentStatus documentStatus = documentService.getDocumentMetadata(documentId).get().getApprovalStatus();
        String documentIdAsString = documentService.getDocumentMetadata(documentId).get().getUser().getId().toString();

        UserRole userRole = getLoggedInUser().getRole();
        String userIdAsString = WebUtils.getLoggedInUser().getId().toString();

        if(documentStatus != DocumentStatus.APPROVED){
            if( userRole != UserRole.ADMIN){
                if(!userIdAsString.equals(documentIdAsString)){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean accessDocumentChanges(UUID documentId){
        String documentIdAsString = documentService.getDocumentMetadata(documentId).get().getUser().getId().toString();

        UserRole userRole = getLoggedInUser().getRole();
        String userIdAsString = WebUtils.getLoggedInUser().getId().toString();

        if( userRole != UserRole.ADMIN){
            if(!userIdAsString.equals(documentIdAsString)){
                return false;
            }
        }
        return true;

    }

    public boolean accessDocumentStatusChanges(){
        if(WebUtils.getLoggedInUser().getRole() != UserRole.ADMIN){
            return false;
        }
        return true;
    }

}
