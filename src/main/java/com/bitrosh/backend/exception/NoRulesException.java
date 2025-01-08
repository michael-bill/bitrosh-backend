package com.bitrosh.backend.exception;

import org.springframework.http.HttpStatus;

public class NoRulesException extends BitroshException {
    public NoRulesException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}
