package com.bitrosh.backend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.MessageDto;
import com.bitrosh.backend.service.MessagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
@Tag(name = "Работа с сообщениями")
public class MessageController {

    private final MessagesService messagesService;

    @Operation(summary = "Получить список сообщений")
    @GetMapping("get-all")
    public Page<MessageDto> getMessages(
            @AuthenticationPrincipal User user,
            @Parameter(description = "Номер страницы (начинается с 0)")
            @RequestParam(defaultValue = "0")
            Integer page,

            @Parameter(description = "Количество элементов на странице")
            @RequestParam(defaultValue = "10")
            Integer size,

            @Parameter(description = "Id чата")
            @RequestParam("chat_id")
            Long chatId,

            @Parameter(description = "Дата, старше которой будут возвращаться сообщения, по умолчанию now()")
            @RequestParam(value = "cutoff_time", required = false)
            Optional<LocalDateTime> cutoffTime
    ) {
        return messagesService.getMessages(
                user,
                chatId,
                cutoffTime.orElse(LocalDateTime.now()),
                PageRequest.of(page, size)
        );
    }

    @Operation(summary = "Отправить сообщение")
    @PostMapping(value = "send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public MessageDto sendMessage(
            @AuthenticationPrincipal User user,

            @Parameter(description = "Id чата")
            @RequestParam("chat_id")
            Long chatId,

            @Parameter(description = "Текст сообщения", required = false)
            @RequestParam(value = "text_content", required = false)
            String textContent,

            @Parameter(description = "Файл")
            @RequestBody(required = false)
            MultipartFile file
    ) throws Exception {
        return messagesService.sendMessage(user, chatId, textContent, file);
    }

    @Operation(summary = "Редактировать сообщение")
    @PostMapping("edit")
    public MessageDto editMessage(
            @AuthenticationPrincipal User user,

            @Parameter(description = "Id сообщения")
            @RequestParam("message_id")
            Long messageId,

            @Parameter(description = "Текст сообщения", required = false)
            @RequestParam(value = "text_content", required = false)
            String textContent
    ) {
        return messagesService.editMessage(user, messageId, textContent);
    }

    @Operation(summary = "Прочитать сообщения старше указанной даты")
    @PostMapping("read")
    public void readMessage(
            @AuthenticationPrincipal User user,

            @Parameter(description = "Id чата")
            @RequestParam("chat_id")
            Long chatId,

            @Parameter(description = "Дата, старше которой будут читаться сообщения, по умолчанию now()")
            @RequestParam(value = "cutoff_date", required = false)
            Optional<LocalDateTime> cutoffTime
    ) {
        messagesService.readMessages(user, chatId, cutoffTime.orElse(LocalDateTime.now()));
    }

    @Operation(summary = "Удалить сообщение")
    @DeleteMapping("delete")
    public void deleteMessage(
            @AuthenticationPrincipal User user,

            @Parameter(description = "Id сообщения")
            @RequestParam("message_id")
            Long messageId
    ) {
        messagesService.deleteMessage(user, messageId);
    }

    @Operation(summary = "Скачать файл из сообщения")
    @GetMapping("/file/download")
    public ResponseEntity<InputStreamResource> downloadFile(
            @AuthenticationPrincipal User user,

            @Parameter(description = "Id сообщения с файлом")
            @RequestParam("message_id")
            Long messageId
    ) throws Exception {
        return messagesService.downloadFile(user, messageId);
    }
}
