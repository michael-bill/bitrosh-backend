package com.bitrosh.backend.dao.projection;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatProjection {
    Long getId();
    WorkspaceProjection getWorkspace();
    String getType();
    String getTitle();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    String getLastMessage();
    List<FolderProjection> getFolders();
    List<UserProjection> getParticipants();
}
