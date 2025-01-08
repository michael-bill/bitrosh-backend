package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Роли пользователей в рабочем пространстве " +
        "(Админ, пользователь с правом записи, пользователь с правами чтения)")
public enum WorkspaceRoleDto {
    ADMIN, USER_RW, USER_RO
}
