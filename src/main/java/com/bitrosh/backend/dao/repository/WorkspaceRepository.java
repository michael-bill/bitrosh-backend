package com.bitrosh.backend.dao.repository;

import com.bitrosh.backend.dao.entity.Workspace;
import com.bitrosh.backend.dao.projection.WorkspaceProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, String> {
    @Query("select w.name as name, w.title as title, w.createdAt as createdAt, uw.role.name as role " +
            "from Workspace w join UserWorkspace uw on w.name = uw.workspace.id " +
            "where uw.user.id = :userId")
    Page<WorkspaceProjection> findAllWorkspacesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select w.name as name, w.title as title, w.createdAt as createdAt, uw.role.name as role " +
            "from Workspace w join UserWorkspace uw on w.name = uw.workspace.id")
    Page<WorkspaceProjection> findAllWorkspaces(Pageable pageable);

    boolean existsByName(String name);
}
