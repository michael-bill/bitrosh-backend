package com.bitrosh.backend.dto.core;

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
@Schema(description = "Информация о колонке на канбан доске")
public class BoardColumnReqDto {
    @Schema(description = "Название рабочего пространства")
    private String workspaceName;
    @Schema(description = "Название колонки")
    private String title;
    @Schema(description = "Уровень колонки")
    private Integer level;
    @Schema(description = "Цвет колонки", example = "#FF0000")
    private String color;
}
