package com.bitrosh.backend.dao.repository;

import java.time.LocalDateTime;

import com.bitrosh.backend.dao.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m where m.chat.id = :chatId and m.createdAt >= :cutoffTime order by m.createdAt")
    Page<Message> findMessages(Long chatId, LocalDateTime cutoffTime, Pageable pageable);

}
