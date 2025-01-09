package com.bitrosh.backend.exception;

import org.springframework.http.HttpStatus;

public class IllegalOperationException extends BitroshException {
    public IllegalOperationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
