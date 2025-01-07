package com.bitrosh.backend.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponseDto {
    private String message;
    private Integer status;
    private LocalDateTime timestamp;
}
