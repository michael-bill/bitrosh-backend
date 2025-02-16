package com.bitrosh.backend.dao.repository;

import java.util.List;
import java.util.Optional;

import com.bitrosh.backend.dao.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {

    Optional<ChatUser> findByChatIdAndUserId(Long chatId, Long userId);

    boolean existsByChatIdAndUserId(Long chatId, Long userId);

    List<ChatUser> findByChatId(Long chatId);

    @Query("""
            select cu
            from ChatUser cu
            where cu.chat.id in (select m.chatId from Message m where m.id = :messageId)
            """)
    List<ChatUser> findByMessageId(Long messageId);

    void deleteByChatIdAndUserId(Long chatId, Long userId);

    @Query(value = """
            select exists (
                select 1 from chat c
                join chat_user cu1 on c.id = cu1.chat_id
                join chat_user cu2 on c.id = cu2.chat_id
                where c.type = 'PRIVATE'
                    and c.workspace_id = :workspaceName
                    and cu1.user_id = :userIdOne
                    and cu2.user_id = :userIdTwo
            )
            """,
            nativeQuery = true
    )
    boolean isPrivateChatAlreadyExists(Long userIdOne, Long userIdTwo, String workspaceName);
}
