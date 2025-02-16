package com.bitrosh.backend.service;

import java.io.IOException;
import java.time.LocalDateTime;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.Message;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.repository.MessageRepository;
import com.bitrosh.backend.dto.core.MessageDto;
import com.bitrosh.backend.exception.IllegalOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor

public class MessagesService {

    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final MinioService minioService;
    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public Page<MessageDto> getMessages(
            User user,
            Long chatId,
            LocalDateTime cutoffTime,
            Pageable pageable
    ) {
        if (!chatService.isUserExistsInChat(chatId, user.getId())) {
            throw new IllegalOperationException("Вы не можете просматривать сообщения чата, в котором не состоите");
        }
        return dtoMapper.map(messageRepository.findMessages(chatId, cutoffTime, pageable), MessageDto.class);
    }

    @Transactional(rollbackFor = {Exception.class, Error.class})
    public void sendMessage(
            User user,
            Long chatId,
            String textContent,
            MultipartFile file
    ) throws Exception {
        if (!chatService.isUserExistsInChat(chatId, user.getId())) {
            throw new IllegalOperationException("Вы не можете отправлять сообщения в чат, в котором не состоите");
        }

        if (textContent == null && (file == null || file.isEmpty())) {
            throw new IllegalOperationException("Вы не можете Отправить пустое сообщение в чат");
        }

        Message message = Message.builder()
                .chatId(chatId)
                .sender(user)
                .textContent(textContent)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isRead(false)
                .build();

        message = messageRepository.save(message);

        if (file != null) {
            String path = "chat_" + chatId + "/message_" + message.getId() + "/" + file.getOriginalFilename();

            message.setFileName(file.getOriginalFilename());
            message.setFilePath(path);
            messageRepository.save(message);

            minioService.uploadFile(
                    path,
                    file.getInputStream(),
                    file.getSize(),
                    file.getContentType()
            );
        }
    }

}
