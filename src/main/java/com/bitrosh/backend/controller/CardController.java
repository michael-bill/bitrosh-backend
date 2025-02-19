package com.bitrosh.backend.controller;

import java.util.List;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.BoardColumnReqDto;
import com.bitrosh.backend.dto.core.BoardColumnResDto;
import com.bitrosh.backend.dto.core.CardReqDto;
import com.bitrosh.backend.dto.core.CardResDto;
import com.bitrosh.backend.service.CardService;
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
@RequestMapping("/board")
@Tag(name = "Работа с канбан доской")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Получить список карточек по колонке")
    @GetMapping("/cards/column")
    public List<CardResDto> getCardsByColumn(
            @AuthenticationPrincipal User user,
            @RequestParam("board_column_id") Long boardColumnId
    ) {
        return cardService.getCardsByBoardColumn(user, boardColumnId);
    }

    @Operation(summary = "Получить список карточек по рабочему пространству")
    @GetMapping("/cards/workspace")
    public List<CardResDto> getCardsByWorkspace(
            @AuthenticationPrincipal User user,
            @RequestParam("workspace_name") String workspaceName
    ) {
        return cardService.getCardsByWorkspace(user, workspaceName);
    }

    @Operation(summary = "Получить карточку по id")
    @GetMapping("/card")
    public CardResDto getCard(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        return cardService.getCard(user, id);
    }

    @Operation(summary = "Создать карточку")
    @PostMapping("/card")
    public CardResDto createCard(
            @AuthenticationPrincipal User user,
            @RequestBody CardReqDto cardReqDto
    ) {
        return cardService.createCard(user, cardReqDto);
    }

    @Operation(summary = "Обновить карточку")
    @PutMapping("/card")
    public CardResDto updateCard(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id,
            @RequestBody CardReqDto cardReqDto
    ) {
        return cardService.updateCard(user, id, cardReqDto);
    }

    @Operation(summary = "Удалить карточку")
    @DeleteMapping("/card")
    public void deleteCard(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        cardService.deleteCard(user, id);
    }
}
