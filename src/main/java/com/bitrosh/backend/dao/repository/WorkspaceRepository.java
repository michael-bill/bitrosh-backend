package com.bitrosh.backend.dao.repository;

import java.util.List;

import com.bitrosh.backend.dao.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, String> {
    @Query("select w from Workspace w join UserWorkspace uw on w.name = uw.workspace.id where uw.user.id = :userId")
    Page<Workspace> findAllWorkspacesByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("select w from Workspace w join UserWorkspace uw on w.name = uw.workspace.id")
    Page<Workspace> findAllWorkspaces(@Param("userId") Long userId, Pageable pageable);
}
