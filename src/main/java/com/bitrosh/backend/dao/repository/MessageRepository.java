package com.bitrosh.backend.dao.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bitrosh.backend.dao.entity.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Modifying
    @Query("""
            update Message m
            set m.isRead = true
            where
                m.chatId = :chatId
                and m.createdAt <= :cutoffTime
            """)
    void readOlderThan(Long chatId, LocalDateTime cutoffTime);
}
