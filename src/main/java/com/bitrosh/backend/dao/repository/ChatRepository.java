package com.bitrosh.backend.dao.repository;

import java.util.List;

import com.bitrosh.backend.dao.entity.Chat;
import com.bitrosh.backend.dao.projection.ChatProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
    @Query(
        value = "select * from find_chats_by_user_and_workspace_f(:userId, :workspaceName)",
        nativeQuery = true
    )
    List<ChatProjection> findChatsByUserAndWorkspace(
            @Param("userId") Long userId,
            @Param("workspaceName") String workspaceName
    );
}
