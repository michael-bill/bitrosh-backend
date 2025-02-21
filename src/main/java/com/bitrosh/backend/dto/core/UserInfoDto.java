package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Информация о пользователе")
public class UserInfoDto {
    @Schema(description = "Id пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "John")
    private String username;
    @Schema(description = "Роль в системе", example = "USER")
    private String role;
}
