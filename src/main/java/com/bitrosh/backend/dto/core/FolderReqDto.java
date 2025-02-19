package com.bitrosh.backend.dto.core;

import java.util.Set;

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
@Schema(description = "Запрос создания папки с чатами")
public class FolderReqDto {
    @Schema(description = "Название папки")
    private String name;
    @Schema(description = "Название рабочего пространства")
    private String workspaceName;
    @Schema(description = "Id чатов, которые будут в папке")
    private Set<Long> chatIds;
}
