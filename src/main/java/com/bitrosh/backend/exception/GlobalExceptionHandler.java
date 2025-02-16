package com.bitrosh.backend.exception;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import com.bitrosh.backend.dto.messages.ErrorResponseDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponseDto.builder()
                        .message("Произошло что-то страшное и непердвиденное: " + ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(BitroshException.class)
    protected ResponseEntity<ErrorResponseDto> handleBitroshException(BitroshException ex) {
        return ResponseEntity.status(ex.getStatus())
                .body(ErrorResponseDto.builder()
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<ErrorResponseDto> handleValidationException(ConstraintViolationException ex) {
        return new ResponseEntity<>(
                ErrorResponseDto.builder()
                        .message(ex.getConstraintViolations().stream()
                                .map(ConstraintViolation::getMessage)
                                .collect(Collectors.joining(", ")))
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>(
                ErrorResponseDto.builder()
                        .message(ex.getBindingResult().getAllErrors().stream()
                                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                                .collect(Collectors.joining(", ")))
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
