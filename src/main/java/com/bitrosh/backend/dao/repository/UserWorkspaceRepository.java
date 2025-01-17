package com.bitrosh.backend.dao.repository;

import com.bitrosh.backend.dao.entity.UserWorkspace;
import com.bitrosh.backend.dao.entity.ids.UserWorkspaceId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, UserWorkspaceId> {
    boolean existsByUserIdAndWorkspaceName(Long userId, String workspaceName);
    boolean existsByUserIdAndWorkspaceNameAndRoleName(Long userId, String workspaceName, String roleName);
    Page<UserWorkspace> findByWorkspaceName(String workspaceName, Pageable pageable);
}
