package com.bitrosh.backend.dao.projection;

import java.time.LocalDateTime;

public interface ChatProjection {
    Long getId();
    String getType();
    String getTitle();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    String getLastMessageText();
    LocalDateTime getLastMessageTime();
    Long getLastMessageSenderId();
    String getFoldersJsonArray(); // Postgres JSONB можно обрабатывать как String
    String getParticipantsJsonArray(); // Postgres JSONB можно обрабатывать как String
}
