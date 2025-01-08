package com.bitrosh.backend.exception;

import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends BitroshException {
    public EntityNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
