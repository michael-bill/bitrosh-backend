package com.bitrosh.backend.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(description = "Ответ с информацией о папке")
public class FolderResDto {
    @Schema(description = "Id папки", example = "1")
    private Long id;
    @Schema(description = "Название папки", example = "My Folder")
    private String name;
}
