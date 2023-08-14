package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.EmailDTO;
import com.springframework.documentmanagementapp.model.ResetPasswordDTO;
import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.model.UserRegistrationDTO;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import com.springframework.documentmanagementapp.services.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.springframework.documentmanagementapp.services.UserRegistrationService.RESET_URL;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    public static final String AUTH_PATH = "/api/v1/authentication";

    private final UserRegistrationService userRegistrationService;


    @PostMapping(AUTH_PATH + "/register")
    public ResponseEntity<String> register(@RequestBody UserRegistrationDTO userDTO){
        return ResponseEntity.ok(userRegistrationService.register(userDTO));
    }

    @GetMapping(AUTH_PATH +"/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token){
        return ResponseEntity.ok(userRegistrationService.confirm(token));
    }

    @PostMapping(AUTH_PATH + "/reset")
    public ResponseEntity<String> initiateResetPassword(@RequestBody EmailDTO emailDTO){
        return ResponseEntity.ok(userRegistrationService.resetPasswordLinkOnEmail(emailDTO,RESET_URL));
    }

    @PostMapping(AUTH_PATH +"/reset/password")
    public ResponseEntity<String> confirmResetPassword(@RequestParam String token, @RequestBody ResetPasswordDTO passwordDTO){
        return ResponseEntity.ok(userRegistrationService.ResetPassword(token, passwordDTO));
    }



}
