package com.bitrosh.backend.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.Message;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.repository.MessageRepository;
import com.bitrosh.backend.dto.core.MessageDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.IllegalOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor

public class MessagesService {

    // chat_<id чата>/message<id сообещния>/название файла
    private static final String FILE_PATH_MASK = "chat_%d/message_%d/%s";

    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final MinioService minioService;
    private final DtoMapper dtoMapper;
    private final WebSocketService webSocketService;

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
    public MessageDto sendMessage(
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
                .isDeleted(false)
                .build();

        message = messageRepository.save(message);

        if (file != null) {
            String path = FILE_PATH_MASK.formatted(chatId, message.getId(), file.getOriginalFilename());

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

        MessageDto res = dtoMapper.map(message, MessageDto.class);

        chatService.getParticipants(chatId).forEach(x -> webSocketService.notifyCreate(x.getUsername(), res));

        return res;
    }

    @Transactional(readOnly = true)
    public ResponseEntity<InputStreamResource> downloadFile(User user, Long messageId) throws Exception {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Сообщение с таким id не было найдено"));
        if (!chatService.isUserExistsInChat(message.getChatId(), user.getId())) {
            throw new IllegalOperationException("Вы не можете скачивать файлы с сообщений, " +
                    "которые состоят в чатах, в которых вас нет");
        }
        if (message.getFilePath() == null) {
            throw new IllegalOperationException("У этого сообщения нет файла");
        }
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + message.getFileName() + "\"")
                .body(new InputStreamResource(minioService.getFile(message.getFilePath())));
    }

    @Transactional
    public void readMessages(User user, Long chatId, LocalDateTime cutoffTime) {
        if (!chatService.isUserExistsInChat(chatId, user.getId())) {
            throw new IllegalOperationException("Вы не можете читать сообщения из чатов, в которых вас нет");
        }
        List<Message> unreaded = messageRepository.findUnreadedOlderThan(chatId, cutoffTime);
        unreaded.forEach(x -> x.setIsRead(true));
        messageRepository.saveAll(unreaded);
        chatService.getParticipants(chatId)
                .forEach(u -> dtoMapper.map(unreaded, MessageDto.class)
                        .forEach(m -> webSocketService.notifyUpdate(u.getUsername(), m)));
    }

    @Transactional
    public void deleteMessage(User user, Long messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new EntityNotFoundException("Сообщение с таким id не было найдено"));
        if (!chatService.isUserExistsInChat(message.getChatId(), user.getId())) {
            throw new IllegalOperationException("Вы не можете удалять сообщения из чатов, в которых вас нет");
        }
        message.setIsDeleted(true);
        messageRepository.save(message);
        chatService.getParticipantsByMessageId(messageId)
                .forEach(x -> webSocketService.notifyDelete(
                        x.getUsername(), dtoMapper.map(message, MessageDto.class)));
    }

}
