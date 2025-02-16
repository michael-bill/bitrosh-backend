package com.bitrosh.backend.dao.projection;

import java.time.LocalDateTime;

public interface WorkspaceProjection {
    String getName();
    String getTitle();
    LocalDateTime getCreatedAt();
    String getWorkspaceRole();
}
