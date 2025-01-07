package com.bitrosh.backend.dao.entity;

import com.bitrosh.backend.dao.entity.ids.UserWorkspaceId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(UserWorkspaceId.class)
@Table(name = "user_workspace")
public class UserWorkspace {
    @Id
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @ManyToOne
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    @JoinColumn(name = "role_id")
    @ManyToOne
    private Role role;
}
