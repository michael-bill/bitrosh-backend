package com.bitrosh.backend.dto.auth;

import com.bitrosh.backend.dto.core.WorkspaceResDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
@Schema(description = "Ответ c токеном доступа")
public class JwtAuthDto {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String token;
    @Schema(description = "Id пользователя", example = "1")
    private Long userId;
    @Schema(description = "Имя пользователя", example = "John")
    private String username;
    @Schema(description = "Роль в системе", example = "USER")
    private String role;
    @Schema(description = "Текущее рабочее пространство")
    private WorkspaceResDto currentWorkspace;
}
