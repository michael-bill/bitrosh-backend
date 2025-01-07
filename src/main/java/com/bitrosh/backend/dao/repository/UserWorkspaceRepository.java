package com.bitrosh.backend.dao.repository;

import com.bitrosh.backend.dao.entity.UserWorkspace;
import com.bitrosh.backend.dao.entity.ids.UserWorkspaceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, UserWorkspaceId> {
}
