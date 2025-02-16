package com.bitrosh.backend.dto.core;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
    private Long id;
    private Long chatId;
    private UserInfoDto sender;
    private String textContent;
    private String fileName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isRead;
}
