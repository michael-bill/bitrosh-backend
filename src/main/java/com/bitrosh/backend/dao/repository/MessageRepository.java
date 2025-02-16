package com.bitrosh.backend.dao.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.bitrosh.backend.dao.entity.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
            select m
            from Message m
            where
                m.chatId = :chatId
                and m.createdAt <= :cutoffTime
                and not m.isDeleted
            order by m.createdAt
            """)
    Page<Message> findMessages(Long chatId, LocalDateTime cutoffTime, Pageable pageable);

    @NotNull
    @Query("select m from Message m where m.id = :messageId and m.isDeleted = false")
    Optional<Message> findById(@NotNull Long messageId);

    @Query("""
            select m
            from Message m
            where
                m.chatId = :chatId
                and m.createdAt <= :cutoffTime
                and not m.isRead
                and not m.isDeleted
            """)
    List<Message> findUnreadedOlderThan(Long chatId, LocalDateTime cutoffTime);
}
