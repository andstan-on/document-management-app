package com.springframework.documentmanagementapp.exception;

public class PasswordsDoNotMatchException extends RuntimeException{
    public PasswordsDoNotMatchException(String message) {
        super(message);
    }
}
