package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на создание приватного чата (чат с 1 пользователем)")
public class ChannelCreationDto {
    @Schema(description = "Название рабочего пространства", example = "workspace1")
    private String workspaceName;
    @Schema
    private String channelName;
}
