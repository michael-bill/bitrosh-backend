package com.bitrosh.backend.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.Chat;
import com.bitrosh.backend.dao.entity.Folder;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.entity.Workspace;
import com.bitrosh.backend.dao.repository.ChatRepository;
import com.bitrosh.backend.dao.repository.FolderRepository;
import com.bitrosh.backend.dto.core.FolderReqDto;
import com.bitrosh.backend.dto.core.FolderResDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.NoRulesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final WorkspaceService workspaceService;
    private final DtoMapper dtoMapper;
    private final ChatRepository chatRepository;

    @Transactional
    public FolderResDto createFolder(User user, FolderReqDto folderReqDto) {
        if (workspaceService.hasNoRulesForWorkspace(user, folderReqDto.getWorkspaceName())) {
            throw new NoRulesException("У пользователя нет прав на создание папки в данном рабочем пространстве");
        }
        return dtoMapper.map(folderRepository.save(Folder.builder()
                .user(user)
                .name(folderReqDto.getName())
                .workspace(Workspace.builder().name(folderReqDto.getWorkspaceName()).build())
                .chats(folderReqDto.getChatIds().stream()
                        .map(chatId -> Chat.builder().id(chatId).build()).collect(Collectors.toSet()))
                .build()), FolderResDto.class);
    }

    @Transactional
    public FolderResDto updateFolder(User user, Long id, FolderReqDto folderReqDto) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Папка не найдена"));
        if (!Objects.equals(folder.getUser().getId(), user.getId())) {
            throw new NoRulesException("У пользователя нет прав на изменение не своей папки");
        }
        if (workspaceService.hasNoRulesForWorkspace(user, folderReqDto.getWorkspaceName())) {
            throw new NoRulesException("У пользователя нет прав на перемещение папки в данное рабочее пространство");
        }
        return dtoMapper.map(folderRepository.save(Folder.builder()
                .id(id)
                .user(user)
                .name(folderReqDto.getName())
                .workspace(Workspace.builder().name(folderReqDto.getWorkspaceName()).build())
                .chats(folderReqDto.getChatIds().stream()
                        .map(chatId -> chatRepository.findById(chatId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        "Чат с id %d не существует".formatted(chatId))))
                        .collect(Collectors.toSet()))
                .build()), FolderResDto.class);
    }

    @Transactional
    public void deleteFolder(User user, Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Папка не найдена"));
        if (!Objects.equals(folder.getUser().getId(), user.getId())) {
            throw new NoRulesException("У пользователя нет прав на удаление не своей папки");
        }
        folderRepository.delete(folder);
    }

    @Transactional(readOnly = true)
    public FolderResDto getFolder(User user, Long id) {
        Folder folder = folderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Папка не найдена"));
        if (!Objects.equals(folder.getUser().getId(), user.getId())) {
            throw new NoRulesException("У пользователя нет прав на просмотр не своей папки");
        }
        return dtoMapper.map(folder, FolderResDto.class);
    }

    @Transactional(readOnly = true)
    public List<FolderResDto> getFolders(User user) {
        return folderRepository.findAllByUserId(user.getId()).stream()
                .map(folder -> dtoMapper.map(folder, FolderResDto.class))
                .collect(Collectors.toList());
    }
}
