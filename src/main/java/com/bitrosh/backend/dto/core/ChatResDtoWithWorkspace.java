package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Schema(description = "Ответ с информацией о чате + workspace")
public class ChatResDtoWithWorkspace extends ChatResDto {
    @Schema(description = "Рабочее пространство с вашей ролью", example = "workspace1")
    private WorkspaceResDto workspace;
}
