package com.bitrosh.backend.controller;

import java.util.List;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.FolderReqDto;
import com.bitrosh.backend.dto.core.FolderResDto;
import com.bitrosh.backend.service.FolderService;
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
@RequestMapping("/folder")
@Tag(name = "Работа с папками")
public class FolderController {

    private final FolderService folderService;

    @Operation(summary = "Получить список папок")
    @GetMapping("/list")
    public List<FolderResDto> getFolders(
            @AuthenticationPrincipal User user
    ) {
        return folderService.getFolders(user);
    }

    @Operation(summary = "Получить папку по id")
    @GetMapping()
    public FolderResDto getFolder(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        return folderService.getFolder(user, id);
    }

    @Operation(summary = "Создать папку")
    @PostMapping
    public FolderResDto createFolder(
            @AuthenticationPrincipal User user,
            @RequestBody FolderReqDto folderReqDto
    ) {
        return folderService.createFolder(user, folderReqDto);
    }

    @Operation(summary = "Обновить папку")
    @PutMapping
    public FolderResDto updateFolder(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id,
            @RequestBody FolderReqDto folderReqDto
    ) {
        return folderService.updateFolder(user, id, folderReqDto);
    }

    @Operation(summary = "Удалить папку")
    @DeleteMapping
    public void deleteFolder(
            @AuthenticationPrincipal User user,
            @RequestParam("id") Long id
    ) {
        folderService.deleteFolder(user, id);
    }
}
