package com.bitrosh.backend.exception;

import org.springframework.http.HttpStatus;

public class IncorrectLoginDataException extends BitroshException {
    public IncorrectLoginDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
