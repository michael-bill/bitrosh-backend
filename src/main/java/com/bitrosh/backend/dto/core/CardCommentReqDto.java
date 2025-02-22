package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
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
@Schema(description = "Запрос создания комментария к карточке")
public class CardCommentReqDto {

    @Schema(description = "Id карточки", example = "1")
    @NotNull(message = "Id карточки не может быть пустым")
    private Long cardId;

    @Schema(description = "Текст комментария")
    @NotNull(message = "Текст комментария не может быть пустым")
    private String content;

    @Schema(description = "Id комментария, на который отвечает комментарий", example = "1")
    private Long replyToCommentId;

}
