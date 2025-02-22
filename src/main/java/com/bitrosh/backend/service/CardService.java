package com.bitrosh.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.BoardColumn;
import com.bitrosh.backend.dao.entity.Card;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.repository.BoardColumnRepository;
import com.bitrosh.backend.dao.repository.CardRepository;
import com.bitrosh.backend.dao.repository.UserRepository;
import com.bitrosh.backend.dto.core.CardReqDto;
import com.bitrosh.backend.dto.core.CardResDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.NoRulesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final BoardColumnRepository boardColumnRepository;
    private final UserRepository userRepository;
    private final WorkspaceService workspaceService;
    private final DtoMapper dtoMapper;

    @Transactional
    public CardResDto createCard(User user, CardReqDto cardReqDto) {
        BoardColumn boardColumn = boardColumnRepository.findById(cardReqDto.getBoardColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Колонка с таким id не была найдена"));
        if (workspaceService.hasNoRulesForWorkspace(user, boardColumn.getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на создание карточки в данном рабочем пространстве");
        }

        User executor = cardReqDto.getExecutorId() != null ? userRepository.findById(cardReqDto.getExecutorId())
                .orElseThrow(() -> new EntityNotFoundException("Исполнитель с таким id не был найден")) : null;

        return dtoMapper.map(cardRepository.save(Card.builder()
                        .boardColumn(boardColumn)
                        .createdBy(user)
                        .executor(executor)
                        .title(cardReqDto.getTitle())
                        .content(cardReqDto.getContent())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .deadline(cardReqDto.getDeadline())
                .build()), CardResDto.class);
    }

    @Transactional
    public CardResDto updateCard(User user, Long id, CardReqDto cardReqDto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Карточка с таким id не была найдена"));

        BoardColumn boardColumn = boardColumnRepository.findById(cardReqDto.getBoardColumnId())
                .orElseThrow(() -> new EntityNotFoundException("Колонка с таким id не была найдена"));

        if (workspaceService.hasNoRulesForWorkspace(user, boardColumn.getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на изменение карточки в данном рабочем пространстве");
        }

        User executor = cardReqDto.getExecutorId() != null ? userRepository.findById(cardReqDto.getExecutorId())
                .orElseThrow(() -> new EntityNotFoundException("Исполнитель с таким id не был найден")) : null;

        return dtoMapper.map(cardRepository.save(Card.builder()
                .id(id)
                .boardColumn(boardColumn)
                .createdBy(user)
                .executor(executor)
                .title(cardReqDto.getTitle())
                .content(cardReqDto.getContent())
                .createdAt(card.getCreatedAt())
                .updatedAt(LocalDateTime.now())
                .deadline(cardReqDto.getDeadline())
                .build()), CardResDto.class);
    }

    @Transactional(readOnly = true)
    public CardResDto getCard(User user, Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Карточка с таким id не была найдена"));

        if (workspaceService.hasNoRulesForWorkspace(user, card.getBoardColumn().getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на просмотр карточки в данном рабочем пространстве");
        }

        return dtoMapper.map(card, CardResDto.class);
    }

    @Transactional(readOnly = true)
    public List<CardResDto> getCardsByBoardColumn(User user, Long boardColumnId) {
        BoardColumn boardColumn = boardColumnRepository.findById(boardColumnId)
                .orElseThrow(() -> new EntityNotFoundException("Колонка с таким id не была найдена"));

        if (workspaceService.hasNoRulesForWorkspace(user, boardColumn.getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на просмотр карточек в данном рабочем пространстве");
        }

        return dtoMapper.map(cardRepository.findByBoardColumnId(boardColumnId), CardResDto.class);
    }

    @Transactional(readOnly = true)
    public List<CardResDto> getCardsByWorkspace(User user, String workspaceName) {
        if (workspaceService.hasNoRulesForWorkspace(user, workspaceName)) {
            throw new NoRulesException("У пользователя нет прав на просмотр карточек в данном рабочем пространстве");
        }
        return dtoMapper.map(cardRepository.findByWorkspaceName(workspaceName), CardResDto.class);
    }

    @Transactional
    public void deleteCard(User user, Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Карточка с таким id не была найдена"));

        if (workspaceService.hasNoRulesForWorkspace(user, card.getBoardColumn().getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на удаление карточки в данном рабочем пространстве");
        }

        cardRepository.delete(card);
    }
}
