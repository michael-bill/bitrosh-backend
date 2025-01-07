package com.bitrosh.backend.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class BitroshException extends RuntimeException {

    private final HttpStatus status;

    public BitroshException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
