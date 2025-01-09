package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Информация о текущем пользователе")
public class MyUserInfoDto {
    @Schema(description = "Id пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "John")
    private String username;
    @Schema(description = "Роль в системе", example = "USER")
    private String role;
    @Schema(description = "Текущее рабочее пространство", exampleClasses = WorkspaceResDto.class)
    private WorkspaceResDto currentWorkspace;
}
