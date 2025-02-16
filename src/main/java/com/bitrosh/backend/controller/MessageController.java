package com.bitrosh.backend.controller;

import java.time.LocalDateTime;
import java.util.Optional;

import com.bitrosh.backend.dao.entity.Message;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.service.MessagesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/message")
@Tag(name = "Работа с сообщениями")
public class MessageController {

    private final MessagesService messagesService;

    @Operation(summary = "Получить список сообщений")
    @GetMapping("/get-all")
    public Page<Message> getMessages(
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
            @RequestParam(value = "cutoff_date", required = false)
            Optional<LocalDateTime> cutoffDate
    ) {
        return messagesService.getMessages(
                user,
                chatId,
                cutoffDate.orElse(LocalDateTime.now()),
                PageRequest.of(page, size)
        );
    }
}
