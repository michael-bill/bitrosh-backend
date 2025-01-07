package com.bitrosh.backend.dto.messages;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Schema(description = "Ответ с ошибкой")
public class ErrorResponseDto {
    @Schema(description = "Сообщение об ошибке")
    private String message;
    @Schema(description = "Код ошибки")
    private Integer status;
    @Schema(description = "Время возникновения ошибки")
    private LocalDateTime timestamp;
}
