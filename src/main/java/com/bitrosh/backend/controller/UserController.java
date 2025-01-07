package com.bitrosh.backend.controller;

import com.bitrosh.backend.dto.core.UserInfoDto;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Работа с пользователем")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить информацию о пользователе")
    @GetMapping("/info")
    private UserInfoDto getUserInfo(
            @AuthenticationPrincipal User user
    ) {
        return userService.getUserInfo(user);
    }
}
