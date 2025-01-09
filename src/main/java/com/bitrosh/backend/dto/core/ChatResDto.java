package com.bitrosh.backend.dto.core;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ с информацией о чате")
public class ChatResDto {
    @Schema(description = "Id чата", example = "1")
    private Long id;
    @Schema(description = "Рабочее пространство с вашей ролью", example = "workspace1")
    private WorkspaceResDto workspace;
    @Schema(description = "Тип чата", example = "PRIVATE")
    private String type;
    @Schema(description = "Название чата", example = "My Chat")
    private String title;
    @Schema(description = "Создатель чата", example = "user1")
    private String createdBy;
    @Schema(description = "Дата создания чата")
    private LocalDateTime createdAt;
    @Schema(description = "Последнее сообщение в чате", example = "Hello, world")
    private String lastMessageText;
    @Schema(description = "Время последнего сообщения в чате", example = "Hello, world")
    private LocalDateTime lastMessageTime;
    @Schema(description = "Id пользователя, что отправил последнее сообщение в чате", example = "1")
    private Long lastMessageSenderId;
    @Schema(description = "Папки, в которых содержится чат")
    private List<FolderResDto> folders;
    @Schema(description = "Участники чата")
    private List<UserInfoDto> participants;
}
