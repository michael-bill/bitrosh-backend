package com.bitrosh.backend.service;

import java.time.LocalDateTime;

import com.bitrosh.backend.dao.entity.Message;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.repository.MessageRepository;
import com.bitrosh.backend.exception.IllegalOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class MessagesService {

    private final MessageRepository messageRepository;
    private final ChatService chatService;

    public Page<Message> getMessages(
            User user,
            Long chatId,
            LocalDateTime cutoffTime,
            Pageable pageable
    ) {
        if (!chatService.isUserExistsInChat(chatId, user.getId())) {
            throw new IllegalOperationException("Вы не можете просматривать сообщения чата, в котором не состоите");
        }
        return messageRepository.findMessages(chatId, cutoffTime, pageable);
    }

}
