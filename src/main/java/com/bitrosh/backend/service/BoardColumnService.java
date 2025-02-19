package com.bitrosh.backend.service;

import java.util.List;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.BoardColumn;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.entity.Workspace;
import com.bitrosh.backend.dao.repository.BoardColumnRepository;
import com.bitrosh.backend.dto.core.BoardColumnReqDto;
import com.bitrosh.backend.dto.core.BoardColumnResDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.NoRulesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final WorkspaceService workspaceService;
    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public List<BoardColumnResDto> getColumns(User user, String workspaceName) {
        if (workspaceService.hasNoRulesForWorkspace(user, workspaceName)) {
            throw new NoRulesException("У пользователя нет прав на просмотр колонок в рабочем пространстве");
        }
        return dtoMapper.map(boardColumnRepository.findByWorkspaceName(workspaceName), BoardColumnResDto.class);
    }

    @Transactional
    public BoardColumnResDto createColumn(User user, BoardColumnReqDto boardDto) {
        if (workspaceService.hasNoRulesForWorkspace(user, boardDto.getWorkspaceName())) {
            throw new NoRulesException("У пользователя нет прав на создание колонки в этом рабочеим пространстве");
        }
        return dtoMapper.map(boardColumnRepository.save(
                BoardColumn.builder()
                        .workspace(Workspace.builder().name(boardDto.getWorkspaceName()).build())
                        .title(boardDto.getTitle())
                        .level(boardDto.getLevel())
                        .color(boardDto.getColor())
                        .build()),
                BoardColumnResDto.class);
    }

    @Transactional
    public BoardColumnResDto updateColumn(User user, Long id, BoardColumnReqDto boardDto) {
        BoardColumn column = boardColumnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Колонка не найдена"));
        if (workspaceService.hasNoRulesForWorkspace(user, column.getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на изменение колонки в данном рабочем пространстве");
        }
        return dtoMapper.map(boardColumnRepository.save(
                        BoardColumn.builder()
                                .id(id)
                                .workspace(Workspace.builder().name(boardDto.getWorkspaceName()).build())
                                .title(boardDto.getTitle())
                                .level(boardDto.getLevel())
                                .color(boardDto.getColor())
                                .build()),
                BoardColumnResDto.class);
    }

    @Transactional(readOnly = true)
    public BoardColumnResDto getColumn(User user, Long id) {
        BoardColumn column = boardColumnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Колонка не найдена"));
        if (workspaceService.hasNoRulesForWorkspace(user, column.getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на создание колонки в этом рабочеим пространстве");
        }
        return dtoMapper.map(column, BoardColumnResDto.class);
    }

    @Transactional
    public void deleteColumn(User user, Long id) {
        BoardColumn column = boardColumnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Колонка не найдена"));
        if (workspaceService.hasNoRulesForWorkspace(user, column.getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на удаление колонки в данном рабочем пространстве");
        }
        boardColumnRepository.delete(column);
    }
}
