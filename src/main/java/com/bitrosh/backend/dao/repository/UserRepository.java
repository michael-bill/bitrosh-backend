package com.bitrosh.backend.dao.repository;

import java.util.List;
import java.util.Optional;

import com.bitrosh.backend.dao.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    long countByRole(User.Role role);
    @Query(value = """
            select u.*
            from users u
            where u.id != :userId
            and exists (
                select 1
                from user_workspace uw
                where uw.user_id = u.id
                and uw.workspace_id = :workspaceId
            )
            and not exists (
                select 1
                from chat c
                inner join chat_user cu_current on c.id = cu_current.chat_id and cu_current.user_id = :userId
                inner join chat_user cu_other on c.id = cu_other.chat_id and cu_other.user_id = u.id
                where
                    c.type = 'PRIVATE'
                    and c.workspace_id = :workspaceId
            )
            """, nativeQuery = true)
    List<User> findUsersWithoutPrivateChat(Long userId, String workspaceId);
}
