package com.bitrosh.backend.controller;

import java.util.List;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.ChannelCreationDto;
import com.bitrosh.backend.dto.core.ChatResDto;
import com.bitrosh.backend.dto.core.ChatResDtoWithWorkspace;
import com.bitrosh.backend.dto.core.GroupChatCreationDto;
import com.bitrosh.backend.dto.core.PrivateChatCreationDto;
import com.bitrosh.backend.dto.core.UserInfoByChatDto;
import com.bitrosh.backend.dto.core.UserInfoDto;
import com.bitrosh.backend.dto.core.WorkspaceOrChatRoleDto;
import com.bitrosh.backend.dto.types.BooleanDto;
import com.bitrosh.backend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @Operation(summary = "Получить список моих чатов")
    @GetMapping("/my")
    public List<ChatResDto> getMyChats(
            @AuthenticationPrincipal User user,
            @RequestParam("workspace_name") String workspaceName
    ) {
        return chatService.getMyChats(user, workspaceName);
    }

    @Operation(summary = "Создание приватного чата (чат с 1 пользователем)")
    @PostMapping("/create/private")
    public ChatResDtoWithWorkspace createPrivateChat(
            @AuthenticationPrincipal User user,
            @RequestBody PrivateChatCreationDto dto
    ) {
        return chatService.createPrivateChat(user, dto);
    }

    @Operation(summary = "Создание группового чата (чат с несколькими пользователями)")
    @PostMapping("/create/group")
    public ChatResDtoWithWorkspace createGroupChat(
            @AuthenticationPrincipal User user,
            @RequestBody GroupChatCreationDto dto
    ) {
        return chatService.createGroupChat(user, dto);
    }

    @Operation(summary = "Создание канала")
    @PostMapping("/create/channel")
    public ChatResDtoWithWorkspace createChanel(
            @AuthenticationPrincipal User user,
            @RequestBody ChannelCreationDto dto
    ) {
        return chatService.createChannel(user, dto);
    }

    @Operation(summary = "Добавить пользователя в групповой чат")
    @PostMapping("/add/group")
    public ChatResDtoWithWorkspace addUserToGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam("chat_id") Long chatId,
            @RequestParam("role") WorkspaceOrChatRoleDto role
    ) {
        return chatService.addUserToGroupChat(user, userId, username, chatId, role.name());
    }

    @Operation(summary = "Добавить пользователя в канал")
    @PostMapping("/add/channel")
    public ChatResDtoWithWorkspace addUserToChannel(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "user_id", required = false) Long userId,
            @RequestParam(value = "username", required = false) String username,
            @RequestParam("chat_id") Long chatId
    ) {
        return chatService.addUserToChannel(user, userId, username, chatId);
    }

    @Operation(summary = "Удалить пользователя из группового чата или канала")
    @PostMapping("/remove/user")
    public ChatResDtoWithWorkspace removeUserFromGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam("user_id") Long userId,
            @RequestParam("chat_id") Long chatId
    ) {
        return chatService.removeUserFromGroupChatOrChannel(user, userId, chatId);
    }

    @Operation(summary = "Выйти из группового чата или канала")
    @PostMapping("/leave")
    public void leaveFromGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam("chat_id") Long chatId
    ) {
        chatService.leaveFromGroupChatChannel(user, chatId);
    }

    @Operation(summary = "Удалить чат")
    @DeleteMapping("/delete")
    public void deleteChat(
            @AuthenticationPrincipal User user,
            @RequestParam("chat_id") Long chatId
    ) {
        chatService.deleteChat(user, chatId);
    }

    @Operation(summary = "Переименовать групповой чат или канал")
    @PostMapping("/rename")
    public ChatResDtoWithWorkspace renameGroupChat(
            @AuthenticationPrincipal User user,
            @RequestParam("chat_id") Long chatId,
            @RequestParam("new_title") String newTitle
    ) {
        return chatService.renameGroupChatOrChannel(user, chatId, newTitle);
    }

    @Operation(summary = "Получить список пользователей в чате")
    @GetMapping("/{chat_id}/users")
    public List<UserInfoByChatDto> getUsersInChat(
            @AuthenticationPrincipal User user,
            @PathVariable("chat_id") Long chatId
    ) {
        return chatService.getUsersInChat(user, chatId);
    }

    @Operation(summary = "Получить список пользователей, с которыми ещё " +
            "не создан личный чат в рамках заданного рабочего пространства")
    @GetMapping("/{workspace_name}/users/without-private-chat")
    public List<UserInfoDto> getUsersWithoutPrivateChat(
            @AuthenticationPrincipal User user,
            @PathVariable("workspace_name") String workspaceName
    ) {
        return chatService.getUsersWithoutPrivateChat(user, workspaceName);
    }

    @Operation(summary = "Есть ли у пользователя приватный чат с заданным " +
            "пользователем в заданном рабочем пространстве")
    @GetMapping("/{workspace_name}/users/has-private-chat")
    public BooleanDto hasPrivateChat(
            @AuthenticationPrincipal User user,
            @RequestParam("user_id") Long userId,
            @PathVariable("workspace_name") String workspaceName
    ) {
        return BooleanDto.builder()
                .value(chatService.isPrivateChatAlreadyExists(user, userId, workspaceName))
                .build();
    }

}
