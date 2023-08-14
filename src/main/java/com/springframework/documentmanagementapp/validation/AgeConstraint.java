package com.springframework.documentmanagementapp.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AgeConstraintValidator.class)
public @interface AgeConstraint {
    String message() default "Age must be at least 14 years old";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}