package com.bitrosh.backend.dto.core;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Ответ с информацией о рабочем пространстве")
public class WorkspaceResDto {
    @Schema(description = "Имя рабочего пространства", example = "workspace1")
    private String name;
    @Schema(description = "Название рабочего пространства", example = "Workspace 1")
    private String title;
    @Schema(description = "Дата создания рабочего пространства")
    private LocalDateTime createdAt;
    @Schema(description = "Роль в рабочем пространстве")
    private WorkspaceOrChatRoleDto role;
}
