package com.bitrosh.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.Card;
import com.bitrosh.backend.dao.entity.CardComment;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.repository.CardCommentRepository;
import com.bitrosh.backend.dao.repository.CardRepository;
import com.bitrosh.backend.dto.core.CardCommentReqDto;
import com.bitrosh.backend.dto.core.CardCommentResDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.NoRulesException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CardCommentService {

    private final CardCommentRepository cardCommentRepository;
    private final CardRepository cardRepository;
    private final WorkspaceService workspaceService;
    private final DtoMapper dtoMapper;

    @Transactional(readOnly = true)
    public List<CardCommentResDto> getCardComments(User user, Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Карточка с таким id не была найдена"));

        if (workspaceService.hasNoRulesForWorkspace(user, card.getBoardColumn().getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на просмотр карточки в данном рабочем пространстве");
        }

        return dtoMapper.map(cardCommentRepository.findByCardId(cardId), CardCommentResDto.class);
    }

    @Transactional(readOnly = true)
    public CardCommentResDto getCardComment(User user, Long id) {
        CardComment cardComment = cardCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с таким id не был найден"));

        if (workspaceService.hasNoRulesForWorkspace(user, cardComment.getCard().getBoardColumn().getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на просмотр карточки в данном рабочем пространстве");
        }

        return dtoMapper.map(cardComment, CardCommentResDto.class);
    }

    @Transactional
    public CardCommentResDto createCardComment(User user, CardCommentReqDto dto) {
        Card card = cardRepository.findById(dto.getCardId())
                .orElseThrow(() -> new EntityNotFoundException("Карточка с таким id не была найдена"));

        if (dto.getReplyToCommentId() != null && !cardCommentRepository.existsById(dto.getReplyToCommentId())) {
            throw new EntityNotFoundException("Комментарий-ответ с таким id не был найден");
        }

        if (workspaceService.hasNoRulesForWorkspace(user, card.getBoardColumn().getWorkspace().getName())) {
            throw new NoRulesException("У пользователя нет прав на создание карточки в данном рабочем пространстве");
        }

        return dtoMapper.map(cardCommentRepository.save(CardComment.builder()
                        .user(user)
                        .card(Card.builder().id(dto.getCardId()).build())
                        .content(dto.getContent())
                        .replyToCommentId(dto.getReplyToCommentId())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                .build()), CardCommentResDto.class);
    }

    @Transactional
    public CardCommentResDto editCardComment(User user, Long id, CardCommentReqDto dto) {
        CardComment cardComment = cardCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с таким id не был найден"));

        if (!Objects.equals(cardComment.getUser().getId(), user.getId())) {
            throw new NoRulesException("У пользователя нет прав на изменение комментария");
        }

        if (dto.getReplyToCommentId() != null) {
            if (!cardCommentRepository.existsById(dto.getReplyToCommentId())) {
                throw new EntityNotFoundException("Комментарий-ответ с таким id не был найден");
            }
            cardComment.setReplyToCommentId(dto.getReplyToCommentId());
        }

        cardComment.setContent(dto.getContent());
        cardComment.setUpdatedAt(LocalDateTime.now());

        return dtoMapper.map(cardCommentRepository.save(cardComment), CardCommentResDto.class);
    }

    @Transactional
    public void deleteCardComment(User user, Long id) {
        CardComment cardComment = cardCommentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Комментарий с таким id не был найден"));

        if (!Objects.equals(cardComment.getUser().getId(), user.getId())) {
            throw new NoRulesException("У пользователя нет прав на удаление комментария");
        }

        cardCommentRepository.delete(cardComment);
    }
}
