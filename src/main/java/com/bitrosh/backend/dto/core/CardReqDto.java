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
@Schema(description = "Запрос создания карточки на колонке канбан доски")
public class CardReqDto {
    @Schema(description = "Id колонки")
    private Long boardColumnId;
    @Schema(description = "Id исполнителя")
    private Long executorId;
    @Schema(description = "Заголовок карточки")
    private String title;
    @Schema(description = "Содержимое карточки")
    private String content;
    @Schema(description = "Дедлайн карточки")
    private LocalDateTime deadline;
}
