package com.bitrosh.backend.controller;

import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.WorkspaceResDto;
import com.bitrosh.backend.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(name = "Работа с рабочими пространствами")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @Operation(summary = "Получить все рабочие пространства пользователя")
    @GetMapping("/get-all")
    public Page<WorkspaceResDto> getAllWorkspaces(
            @AuthenticationPrincipal User user,
            @Parameter(description = "Номер страницы (начинается с 0)")
            @RequestParam(defaultValue = "0")
            Integer page,
            @Parameter(description = "Количество элементов на странице")
            @RequestParam(defaultValue = "10")
            Integer size
    ) {
        return workspaceService.getAllWorkspaces(user, PageRequest.of(page, size));
    }

}
