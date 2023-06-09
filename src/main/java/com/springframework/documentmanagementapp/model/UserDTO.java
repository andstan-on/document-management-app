package com.springframework.documentmanagementapp.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
public class UserDTO {
    private UUID id;

    @NotBlank
    @NotNull
    private String username;

    private String password;
    private String firstName;
    private String lastName;

    @NotBlank
    @NotNull
    private String email;

    @NotNull
    private LocalDate dateOfBirth;
    private UserRole role;
}
