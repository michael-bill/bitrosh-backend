package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на создание приватного чата (чат с 1 пользователем)")
public class PrivateChatCreationDto {
    @Schema(description = "Название рабочего пространства", example = "workspace1")
    private String workspaceName;
    @Schema(description = "Id пользователя, с которым создается чат", example = "1")
    private Long userId;
}
