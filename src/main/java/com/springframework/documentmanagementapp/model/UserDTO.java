package com.springframework.documentmanagementapp.model;

import com.springframework.documentmanagementapp.validation.AgeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;

    @NotBlank
    @NotNull
    private String username;

    private String password;
    private String firstName;
    private String lastName;

    private Integer version;

    @NotBlank
    @NotNull
    private String email;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    private UserRole role;
}
