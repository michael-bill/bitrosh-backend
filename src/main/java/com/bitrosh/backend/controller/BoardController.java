package com.bitrosh.backend.controller;

import java.util.List;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.BoardColumnReqDto;
import com.bitrosh.backend.dto.core.BoardColumnResDto;
import com.bitrosh.backend.service.BoardColumnService;
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
public class BoardController {

    private final BoardColumnService boardColumnService;

    @Operation(summary = "Получить список колонок по рабочему пространству")
    @GetMapping("columns")
    public List<BoardColumnResDto> getColumns(
            @AuthenticationPrincipal User user,
            @RequestParam("workspace_name") String workspaceName
    ) {
        return boardColumnService.getColumns(user, workspaceName);
    }

    @Operation(summary = "Получить колонку по id")
    @GetMapping("column")
    public BoardColumnResDto getColumn(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        return boardColumnService.getColumn(user, id);
    }

    @Operation(summary = "Создать колонку")
    @PostMapping("column")
    public BoardColumnResDto createColumn(
            @AuthenticationPrincipal User user,
            @RequestBody BoardColumnReqDto boardColumnReqDto
    ) {
        return boardColumnService.createColumn(user, boardColumnReqDto);
    }

    @Operation(summary = "Обновить колонку")
    @PutMapping("column")
    public BoardColumnResDto updateColumn(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id,
            @RequestBody BoardColumnReqDto boardColumnReqDto
    ) {
        return boardColumnService.updateColumn(user, id, boardColumnReqDto);
    }

    @Operation(summary = "Удалить колонку (можно только если на ней нет карточек)")
    @DeleteMapping("column")
    public void deleteColumn(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        boardColumnService.deleteColumn(user, id);
    }
}
