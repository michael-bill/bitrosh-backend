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
@Schema(description = "Комментарий к карточке")
public class CardCommentResDto {
    @Schema(description = "Id комментария")
    private Long id;
    @Schema(description = "Пользователь, который оставил комментарий")
    private UserInfoDto user;
    @Schema(description = "Карточка, к которой оставлен комментарий")
    private CardResDto card;
    @Schema(description = "Текст комментария")
    private String content;
    @Schema(description = "Id комментария, на который отвечает комментарий")
    private Long replyToCommentId;
    @Schema(description = "Дата создания комментария")
    private LocalDateTime createdAt;
    @Schema(description = "Дата обновления комментария")
    private LocalDateTime updatedAt;
}
