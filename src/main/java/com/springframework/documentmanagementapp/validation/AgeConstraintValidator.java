package com.springframework.documentmanagementapp.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

public class AgeConstraintValidator implements ConstraintValidator<AgeConstraint, LocalDate> {
    private static final int MINIMUM_AGE = 14;

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true; // Null values are handled by @NotNull annotation
        }

        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(dateOfBirth, currentDate);

        return age.getYears() >= MINIMUM_AGE;
    }
}
