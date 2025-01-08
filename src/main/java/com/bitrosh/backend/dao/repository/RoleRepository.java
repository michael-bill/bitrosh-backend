package com.bitrosh.backend.dao.repository;

import java.util.Optional;

import com.bitrosh.backend.dao.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("select r from UserWorkspace uw join uw.role r " +
            "where uw.user.id = :userId and uw.workspace.name = :workspaceName")
    Optional<Role> findByUserIdAndWorkspaceName(Long userId, String workspaceName);
}
