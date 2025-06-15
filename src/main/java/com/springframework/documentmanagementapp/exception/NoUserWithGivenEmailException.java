package com.springframework.documentmanagementapp.exception;

public class NoUserWithGivenEmailException extends RuntimeException {
    public NoUserWithGivenEmailException(String message) {
        super(message);
    }
}
