package com.springframework.documentmanagementapp.model;

import com.springframework.documentmanagementapp.validation.AgeConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    @NotBlank
    @NotNull
    @Size(min = 5, message = "Username must be at least 5 characters long")
    private String username;

    @NotNull
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$",
            message = "Password must have at least one lowercase, one uppercase, one digit, and be at least 8 characters long")
    private String password;
    private String firstName;
    private String lastName;


    @NotBlank
    @NotNull
    private String email;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @AgeConstraint
    private LocalDate dateOfBirth;
}
