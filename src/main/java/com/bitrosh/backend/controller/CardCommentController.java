package com.bitrosh.backend.controller;

import java.util.List;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.CardCommentReqDto;
import com.bitrosh.backend.dto.core.CardCommentResDto;
import com.bitrosh.backend.service.CardCommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card-comments")
@Tag(name = "Работа с комментариями на карточках")
public class CardCommentController {

    private final CardCommentService cardCommentService;

    @Operation(summary = "Получить список комментариев к карточке")
    @GetMapping("/list")
    public List<CardCommentResDto> getCardComments(
            @AuthenticationPrincipal User user,
            @RequestParam("card_id") Long cardId
    ) {
        return cardCommentService.getCardComments(user, cardId);
    }

    @Operation(summary = "Получить комменатрий по id")
    @GetMapping
    public CardCommentResDto getCardComment(
            @AuthenticationPrincipal User user,
            @RequestParam("card_comment_id") Long cardCommentId
    ) {
        return cardCommentService.getCardComment(user, cardCommentId);
    }

    @Operation(summary = "Создать комментарий к карточке")
    @PostMapping
    public CardCommentResDto createCardComment(
            @AuthenticationPrincipal User user,
            @RequestBody CardCommentReqDto cardCommentReqDto
    ) {
        return cardCommentService.createCardComment(user, cardCommentReqDto);
    }

    @Operation(summary = "Обновить комментарий к карточке")
    @PutMapping
    public CardCommentResDto updateCardComment(
            @AuthenticationPrincipal User user,
            @RequestParam("card_comment_id") Long cardCommentId,
            @RequestBody CardCommentReqDto cardCommentReqDto
    ) {
        return cardCommentService.editCardComment(user, cardCommentId, cardCommentReqDto);
    }

    @Operation(summary = "Удалить комментарий к карточке")
    @DeleteMapping
    public void deleteCardComment(
            @AuthenticationPrincipal User user,
            @RequestParam("card_comment_id") Long cardCommentId
    ) {
        cardCommentService.deleteCardComment(user, cardCommentId);
    }
}
