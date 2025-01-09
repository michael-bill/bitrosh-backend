package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Информация о текущем пользователе по рабочему пространству")
public class UserInfoByWorkspaceDto {
    @Schema(description = "Id польователя")
    private Long id;
    @Schema(description = "Имя пользователя", example = "John")
    private String username;
    @Schema(description = "Роль в системе", example = "USER")
    private String role;
    @Schema(description = "Роль в рабочем пространстве", example = "USER_RW")
    private String workspaceRole;
}
