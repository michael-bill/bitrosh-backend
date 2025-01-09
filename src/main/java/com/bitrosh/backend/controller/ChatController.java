package com.bitrosh.backend.controller;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.ChatResDto;
import com.bitrosh.backend.dto.core.GroupChatCreationDto;
import com.bitrosh.backend.dto.core.PrivateChatCreationDto;
import com.bitrosh.backend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
@Tag(name = "Работа с чатами")
public class ChatController {

    private final ChatService chatService;

    @Operation(summary = "Получить список моих чатов (в разработке)")
    @GetMapping("/my")
    public Page<ChatResDto> getMyChats(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "workspace_name") String workspaceName,
            @Parameter(description = "Номер страницы (начинается с 0)")
            @RequestParam(defaultValue = "0")
            Integer page,
            @Parameter(description = "Количество элементов на странице")
            @RequestParam(defaultValue = "10")
            Integer size
    ) {
        // TODO:
        throw new UnsupportedOperationException("Падажи брат, ещё работаем");
    }

    @Operation(summary = "Создание приватного чата (чат с 1 пользователем)")
    @PostMapping("/create/private")
    public ChatResDto createPrivateChat(
            @AuthenticationPrincipal User user,
            @RequestBody PrivateChatCreationDto dto
    ) {
        return chatService.createPrivateChat(user, dto);
    }

    @Operation(summary = "Создание группового чата (чат с несколькими пользователями)")
    @PostMapping("/create/group")
    public ChatResDto createGroupChat(
            @AuthenticationPrincipal User user,
            @RequestBody GroupChatCreationDto dto
    ) {
        return chatService.createGroupChat(user, dto);
    }

    @Operation(summary = "Добавить пользователя в группой чат")
    @PostMapping("/add/user")
    public ChatResDto addUserToGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam("user_id") Long userId,
            @RequestParam("chat_id") Long chatId,
            @RequestParam("role") String role
    ) {
        return chatService.addUserToGroupChat(user, userId, chatId, role);
    }

    @Operation(summary = "Удалить пользователя из группового чата")
    @PostMapping("/remove/user")
    public ChatResDto removeUserFromGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam("user_id") Long userId,
            @RequestParam("chat_id") Long chatId
    ) {
        return chatService.removeUserFromGroupChat(user, userId, chatId);
    }

    @Operation(summary = "Выйти из группового чата")
    @PostMapping("/leave")
    public void leaveFromGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam("chat_id") Long chatId
    ) {
        chatService.leaveFromGroupChat(user, chatId);
    }

    @Operation(summary = "Удалить чат")
    @DeleteMapping("/delete")
    public void deleteChat(
            @AuthenticationPrincipal User user,
            @RequestParam("chat_id") Long chatId
    ) {
        chatService.deleteChat(user, chatId);
    }

    @Operation(summary = "Переименовать групповой чат")
    @PostMapping("/rename")
    public ChatResDto renameGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam("chat_id") Long chatId,
            @RequestBody String newTitle
    ) {
        return chatService.renameGroupChat(user, chatId, newTitle);
    }
}
