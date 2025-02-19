package com.bitrosh.backend.dto.core;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Информация о колонке на канбан доске (пока без спринта и комментариев)")
public class CardResDto {
    @Schema(description = "Id карточки")
    private Long id;
    @Schema(description = "Id колонки")
    private BoardColumnResDto boardColumn;
    @Schema(description = "Создатель карточки")
    private UserInfoDto createdBy;
    @Schema(description = "Исполнитель карточки")
    private UserInfoDto executor;
    @Schema(description = "Заголовок карточки")
    private String title;
    @Schema(description = "Содержимое карточки")
    private String content;
    @Schema(description = "Дата создания карточки")
    private LocalDateTime createdAt;
    @Schema(description = "Дата обновления карточки")
    private LocalDateTime updatedAt;
    @Schema(description = "Дедлайн карточки")
    private LocalDateTime deadline;
}
