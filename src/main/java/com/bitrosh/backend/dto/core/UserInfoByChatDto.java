package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о пользователе")
public class UserInfoByChatDto {
    @Schema(description = "Id пользователя", example = "1")
    private Long id;
    @Schema(description = "Имя пользователя", example = "John")
    private String username;
    @Schema(description = "Роль в чате", example = "USER")
    private String chatRole;
}
