package com.bitrosh.backend.dto.messages;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Сообщение")
public class AppMessageDto {
    @Schema(description = "Текст сообщения")
    String message;
}
