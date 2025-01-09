package com.bitrosh.backend.dao.repository;

import java.util.List;
import java.util.Optional;

import com.bitrosh.backend.dao.entity.ChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatUserRepository extends JpaRepository<ChatUser, Long> {
    Optional<ChatUser> findByChatIdAndUserId(Long chatId, Long userId);
    List<ChatUser> findByChatId(Long chatId);
    void deleteByChatIdAndUserId(Long chatId, Long userId);
}
