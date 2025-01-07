package com.bitrosh.backend.exception;

import org.springframework.http.HttpStatus;

public class UniqueValueExistsException extends BitroshException {
    public UniqueValueExistsException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
