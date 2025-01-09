package com.bitrosh.backend.dto.core;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Запрос на создание приватного чата (чат с 1 пользователем)")
public class GroupChatCreationDto {
    @Schema(description = "Название рабочего пространства", example = "workspace1")
    private String workspaceName;
    @Schema(description = "Название чата", example = "My Chat")
    private String title;
    @Schema(description = "Список идентификаторов пользователей, которые будут добавлены в чат")
    private List<Long> userIds;
}
