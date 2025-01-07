package com.bitrosh.backend.exception;

import java.time.LocalDateTime;

import com.bitrosh.backend.dto.messages.ErrorResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BitroshException.class)
    protected ResponseEntity<ErrorResponseDto> handleBitroshException(BitroshException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponseDto.builder()
                        .message(ex.getMessage())
                        .status(ex.getStatus().value())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}
