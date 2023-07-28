package com.springframework.documentmanagementapp.controller;

import com.springframework.documentmanagementapp.model.UserDTO;
import com.springframework.documentmanagementapp.repositories.UserRepository;
import com.springframework.documentmanagementapp.services.UserRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserAuthController {

    public static final String AUTH_PATH = "/api/v1/authentication";

    private final UserRegistrationService userRegistrationService;


    @PostMapping(AUTH_PATH + "/register")
    public ResponseEntity<String> register(@RequestBody UserDTO userDTO){
        return ResponseEntity.ok(userRegistrationService.register(userDTO));
    }

    @GetMapping(AUTH_PATH +"/confirm")
    public ResponseEntity<String> confirm(@RequestParam String token){
        return ResponseEntity.ok(userRegistrationService.confirm(token));
    }

}
