package com.springframework.documentmanagementapp.webutils;

import com.springframework.documentmanagementapp.entities.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class WebUtils {

    public static User getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = (User) authentication.getPrincipal();

        return user;
    }

}
