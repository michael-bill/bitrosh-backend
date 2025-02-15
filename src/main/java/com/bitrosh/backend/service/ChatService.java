package com.bitrosh.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.Chat;
import com.bitrosh.backend.dao.entity.ChatUser;
import com.bitrosh.backend.dao.entity.Role;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.entity.Workspace;
import com.bitrosh.backend.dao.projection.ChatProjection;
import com.bitrosh.backend.dao.repository.ChatRepository;
import com.bitrosh.backend.dao.repository.ChatUserRepository;
import com.bitrosh.backend.dao.repository.RoleRepository;
import com.bitrosh.backend.dao.repository.UserRepository;
import com.bitrosh.backend.dao.repository.WorkspaceRepository;
import com.bitrosh.backend.dto.core.ChatResDto;
import com.bitrosh.backend.dto.core.ChatResDtoWithWorkspace;
import com.bitrosh.backend.dto.core.GroupChatCreationDto;
import com.bitrosh.backend.dto.core.PrivateChatCreationDto;
import com.bitrosh.backend.dto.core.UserInfoDto;
import com.bitrosh.backend.dto.core.WorkspaceOrChatRoleDto;
import com.bitrosh.backend.dto.core.WorkspaceResDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.IllegalOperationException;
import com.bitrosh.backend.exception.NoRulesException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final WorkspaceService workspaceService;
    private final RoleService roleService;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final ChatUserRepository chatUserRepository;
    private final RoleRepository roleRepository;
    private final DtoMapper dtoMapper;
    private final ObjectMapper objectMapper;

    // это пиздец
    public List<ChatResDto> getMyChats(User user, String workspaceName) {
        if (!workspaceRepository.existsByName(workspaceName)) {
            throw new EntityNotFoundException("Рабочее пространство с таким name не найдено");
        }
        if (workspaceService.hasNoRulesForWorkspace(user, workspaceName)) {
            throw new NoRulesException("У вас нет прав на работу с данным рабочим пространством");
        }
        List<ChatProjection> chats = chatRepository.findChatsByUserAndWorkspace(
                user.getId(),
                workspaceName
        );
        return chats.stream().map(x -> {
            try {
                return ChatResDto.builder()
                        .id(x.getId())
                        .type(x.getType())
                        .title(x.getTitle())
                        .createdAt(x.getCreatedAt())
                        .createdBy(x.getCreatedBy())
                        .lastMessageText(x.getLastMessageText())
                        .lastMessageTime(x.getLastMessageTime())
                        .lastMessageSenderId(x.getLastMessageSenderId())
                        .folders(
                                x.getFoldersJsonArray() == null ? null :
                                        objectMapper.readValue(x.getFoldersJsonArray(), new TypeReference<>() { })
                        )
                        .participants(
                                x.getParticipantsJsonArray() == null ? null :
                                        objectMapper.readValue(x.getParticipantsJsonArray(), new TypeReference<>() { })
                        )
                        .build();
            } catch (JsonProcessingException e) {
                // гипотетически, этого не должно произойти
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
    }

    @Transactional
    public ChatResDtoWithWorkspace createPrivateChat(User user, PrivateChatCreationDto dto) {
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceName())
                .orElseThrow(() -> new EntityNotFoundException("Рабочее пространство не найдено"));

        if (workspaceService.hasNoRulesForWorkspace(user, dto.getWorkspaceName())) {
            throw new NoRulesException("У вас нет прав на работу с этим рабочим пространством");
        }

        User userTwo = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));

        if (workspaceService.hasNoRulesForWorkspace(userTwo, dto.getWorkspaceName())) {
            throw new NoRulesException(
                    "Пользователя, с которым вы пытаетесь создать чат, нет в этом рабочем пространстве"
            );
        }

        LocalDateTime now = LocalDateTime.now();

        Chat chat = Chat.builder()
                .workspace(workspace)
                .createdBy(user)
                .createdAt(now)
                .type(Chat.ChatType.PRIVATE)
                .build();
        chat = chatRepository.save(chat);

        ChatUser chatUserOne = ChatUser.builder()
                .chat(chat)
                .user(user)
                .joinAt(now)
                .role(roleService.getCachedByName(WorkspaceOrChatRoleDto.ADMIN.name()))
                .build();

        ChatUser chatUserTwo = ChatUser.builder()
                .chat(chat)
                .user(userTwo)
                .joinAt(now)
                .role(roleService.getCachedByName(WorkspaceOrChatRoleDto.ADMIN.name()))
                .build();

        chatUserRepository.save(chatUserOne);
        chatUserRepository.save(chatUserTwo);

        return ChatResDtoWithWorkspace.builder()
                .id(chat.getId())
                .workspace(dtoMapper.map(workspace, WorkspaceResDto.class))
                .type(chat.getType().name())
                .createdBy(user.getUsername())
                .createdAt(chat.getCreatedAt())
                .participants(List.of(
                        dtoMapper.map(user, UserInfoDto.class),
                        dtoMapper.map(userTwo, UserInfoDto.class)
                ))
                .build();
    }

    @Transactional
    public ChatResDtoWithWorkspace createGroupChat(User user, GroupChatCreationDto dto) {
        Workspace workspace = workspaceRepository.findById(dto.getWorkspaceName())
                .orElseThrow(() -> new EntityNotFoundException("Рабочее пространство не найдено"));

        if (workspaceService.hasNoRulesForWorkspace(user, dto.getWorkspaceName())) {
            throw new NoRulesException("У вас нет прав на работу с этим рабочим пространством");
        }

        List<User> users = userRepository.findAllById(dto.getUserIds());

        users.forEach(u -> {
            if (workspaceService.hasNoRulesForWorkspace(u, dto.getWorkspaceName())) {
                throw new NoRulesException(
                        "У пользователя с id = " + u.getId() + ", с которым вы пытаетесь создать чат, " +
                        "нет в этом рабочем пространстве"
                );
            }
        });

        LocalDateTime now = LocalDateTime.now();

        Chat chat = Chat.builder()
                .workspace(workspace)
                .createdBy(user)
                .createdAt(now)
                .title(dto.getTitle())
                .type(Chat.ChatType.GROUP)
                .build();
        chat = chatRepository.save(chat);

        Chat finalChat = chat;
        List<ChatUser> chatUsers = users.stream()
                .map(x -> ChatUser.builder()
                        .chat(finalChat)
                        .user(x)
                        .joinAt(now)
                        .role(roleService.getCachedByName(WorkspaceOrChatRoleDto.USER_RW.name()))
                        .build())
                .toList();

        chatUserRepository.saveAll(chatUsers);

        return ChatResDtoWithWorkspace.builder()
                .id(chat.getId())
                .workspace(dtoMapper.map(workspace, WorkspaceResDto.class))
                .type(chat.getType().name())
                .title(chat.getTitle())
                .createdBy(user.getUsername())
                .createdAt(chat.getCreatedAt())
                .participants(dtoMapper.map(users, UserInfoDto.class))
                .build();
    }

    @Transactional
    public ChatResDtoWithWorkspace addUserToGroupChat(User user, Long userId, Long chatId, String role) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Чат с таким id не найден"));
        Role roleEntity = roleRepository.findByName(role)
                .orElseThrow(() -> new EntityNotFoundException("Роль с таким именем не найдена"));
        User invitedUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));

        if (chat.getType() != Chat.ChatType.GROUP) {
            throw new IllegalOperationException("Можно добавить пользователя только в групповой чат");
        }

        // Пользователь должен меть права хотяб на чтение рабочего пространства
        if (workspaceService.hasNoRulesForWorkspace(user, chat.getWorkspace().getName())) {
            throw new NoRulesException("У вас нет прав на работу с этим рабочим пространством");
        }

        if (workspaceService.hasNoRulesForWorkspace(invitedUser, chat.getWorkspace().getName())) {
            throw new NoRulesException(
                    "Пользователя, которого вы патетесь добавить в чат, нет в этом рабочем пространстве"
            );
        }

        if (!user.isAdmin()) {
            ChatUser chatUserByInviter = chatUserRepository.findByChatIdAndUserId(chatId, user.getId())
                    .orElseThrow(
                            () -> new NoRulesException("Вы не состоите в этом чате, чтобы сюда кого-то приглашать")
                    );

            if (!Objects.equals(chatUserByInviter.getRole().getName(), WorkspaceOrChatRoleDto.ADMIN.name())) {
                throw new NoRulesException("У вас нет прав на добавление пользовтелей в этот чат");
            }

            if (!roleService.moreOrEqualsByName(chatUserByInviter.getRole().getName(), role)) {
                throw new NoRulesException("Вы не имеете право добавлять пользователя в чат с ролью, выше вашей");
            }
        }

        ChatUser newChatUser = ChatUser.builder()
                .chat(chat)
                .user(invitedUser)
                .joinAt(LocalDateTime.now())
                .role(roleEntity)
                .build();
        chatUserRepository.save(newChatUser);

        return ChatResDtoWithWorkspace.builder()
                .id(chat.getId())
                .workspace(dtoMapper.map(chat.getWorkspace(), WorkspaceResDto.class))
                .type(chat.getType().name())
                .title(chat.getTitle())
                .createdBy(user.getUsername())
                .createdAt(chat.getCreatedAt())
                .participants(dtoMapper.map(getParticipants(chat), UserInfoDto.class))
                .build();
    }

    @Transactional
    public ChatResDtoWithWorkspace removeUserFromGroupChat(User user, Long userId, Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Чат с таким id не найден"));
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с таким id не найден"));

        if (chat.getType() != Chat.ChatType.GROUP) {
            throw new IllegalOperationException("Можно удалить пользователя только из группового чата");
        }

        // Пользователь должен меть права хотяб на чтение рабочего пространства
        if (workspaceService.hasNoRulesForWorkspace(user, chat.getWorkspace().getName())) {
            throw new NoRulesException("У вас нет прав на работу с этим рабочим пространством");
        }

        if (!user.isAdmin()) {
            ChatUser chatUserByInviter = chatUserRepository.findByChatIdAndUserId(chatId, user.getId())
                    .orElseThrow(
                            () -> new NoRulesException("Вы не состоите в этом чате, чтобы отсюда кого-то удалять")
                    );

            if (!Objects.equals(chatUserByInviter.getRole().getName(), WorkspaceOrChatRoleDto.ADMIN.name())) {
                throw new NoRulesException("У вас нет прав на удаление пользовтелей из этого чата");
            }
        }

        chatUserRepository.deleteByChatIdAndUserId(chatId, userId);

        return ChatResDtoWithWorkspace.builder()
                .id(chat.getId())
                .workspace(dtoMapper.map(chat.getWorkspace(), WorkspaceResDto.class))
                .type(chat.getType().name())
                .title(chat.getTitle())
                .createdBy(user.getUsername())
                .createdAt(chat.getCreatedAt())
                .participants(dtoMapper.map(getParticipants(chat), UserInfoDto.class))
                .build();
    }

    @Transactional
    public void leaveFromGroupChat(User user, Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Чат с таким id не найден"));
        if (chat.getType() != Chat.ChatType.GROUP) {
            throw new IllegalOperationException("Можно выйти только из группового чата");
        }
        chatUserRepository.deleteByChatIdAndUserId(chatId, user.getId());
    }

    @Transactional
    public void deleteChat(User user, Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Чат с таким id не найден"));

        // Пользователь должен меть права хотяб на чтение рабочего пространства
        if (workspaceService.hasNoRulesForWorkspace(user, chat.getWorkspace().getName())) {
            throw new NoRulesException("У вас нет прав на работу с этим рабочим пространством");
        }

        if (!user.isAdmin()) {
            ChatUser chatUserByInviter = chatUserRepository.findByChatIdAndUserId(chatId, user.getId())
                    .orElseThrow(
                            () -> new NoRulesException("Вы не состоите в этом чате, чтобы его удалить")
                    );

            if (!Objects.equals(chatUserByInviter.getRole().getName(), WorkspaceOrChatRoleDto.ADMIN.name())) {
                throw new NoRulesException("У вас нет прав на удаление этого чата");
            }
        }

        // Каскадное удаление чата, информации по пользователях в этом чате и сообщений
        chatRepository.delete(chat);
    }

    @Transactional
    public ChatResDtoWithWorkspace renameGroupChat(User user, Long chatId, String newTitle) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new EntityNotFoundException("Чат с таким id не найден"));

        if (chat.getType() != Chat.ChatType.GROUP) {
            throw new IllegalOperationException("Можно переименовывать только групповой чат");
        }

        // Пользователь должен меть права хотяб на чтение рабочего пространства
        if (workspaceService.hasNoRulesForWorkspace(user, chat.getWorkspace().getName())) {
            throw new NoRulesException("У вас нет прав на работу с этим рабочим пространством");
        }

        if (!user.isAdmin()) {
            ChatUser chatUserByInviter = chatUserRepository.findByChatIdAndUserId(chatId, user.getId())
                    .orElseThrow(
                            () -> new NoRulesException("Вы не состоите в этом чате, чтобы его переименовывать")
                    );

            if (!Objects.equals(chatUserByInviter.getRole().getName(), WorkspaceOrChatRoleDto.ADMIN.name())) {
                throw new NoRulesException("У вас нет прав на изменения названя этого чата");
            }
        }

        chat.setTitle(newTitle);
        chatRepository.save(chat);

        return ChatResDtoWithWorkspace.builder()
                .id(chat.getId())
                .workspace(dtoMapper.map(chat.getWorkspace(), WorkspaceResDto.class))
                .type(chat.getType().name())
                .title(chat.getTitle())
                .createdBy(user.getUsername())
                .createdAt(chat.getCreatedAt())
                .participants(dtoMapper.map(getParticipants(chat), UserInfoDto.class))
                .build();
    }

    public List<UserInfoDto> getUsersWithoutDirectChat(User user, String workspaceName) {
        if (workspaceService.hasNoRulesForWorkspace(user, workspaceName)) {
            throw new NoRulesException("У вас нет прав на чтение этого рабочего пространства");
        }
        return userRepository.findUsersWithoutPrivateChat(user.getId(), workspaceName)
                .stream().map(u -> dtoMapper.map(u, UserInfoDto.class))
                .toList();
    }

    private List<User> getParticipants(Chat chat) {
        return chatUserRepository.findByChatId(chat.getId())
                .stream()
                .map(ChatUser::getUser)
                .toList();
    }
}
