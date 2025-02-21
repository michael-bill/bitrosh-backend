package com.bitrosh.backend.controller;

import com.bitrosh.backend.dto.auth.CreateUserRequest;
import com.bitrosh.backend.dto.core.MyUserInfoDto;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.UserInfoByWorkspaceDto;
import com.bitrosh.backend.dto.core.UserInfoDto;
import com.bitrosh.backend.dto.types.StringDto;
import com.bitrosh.backend.service.AuthService;
import com.bitrosh.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "Работа с пользователем")
public class UserController {

    private final UserService userService;
    private final AuthService authService;

    @Operation(summary = "Получить информацию о пользователе")
    @GetMapping("/info/me")
    public MyUserInfoDto getUserInfo(
            @AuthenticationPrincipal User user
    ) {
        return userService.getMyUserInfo(user);
    }

    @Operation(summary = "Получить список пользователей по рабочему пространству")
    @GetMapping("/info/list/{workspace_name}")
    public Page<UserInfoByWorkspaceDto> getUserListByWorkspaceName(
            @AuthenticationPrincipal User user,
            @PathVariable("workspace_name") String workspaceName,
            @Parameter(description = "Номер страницы (начинается с 0)")
            @RequestParam(defaultValue = "0")
            Integer page,
            @Parameter(description = "Количество элементов на странице")
            @RequestParam(defaultValue = "10")
            Integer size
    ) {
        return userService.getUserListByWorkspaceName(user, workspaceName, PageRequest.of(page, size));
    }

    @Operation(summary = "Получить список всех пользователей")
    @GetMapping("/info/list/all")
    public Page<UserInfoDto> getAllUsers(
            @PathVariable(name = "username_search", required = false) String usernameKeyWord,
            @Parameter(description = "Номер страницы (начинается с 0)")
            @RequestParam(defaultValue = "0")
            Integer page,
            @Parameter(description = "Количество элементов на странице")
            @RequestParam(defaultValue = "10")
            Integer size
    ) {
        return userService.getAllUsers(usernameKeyWord, PageRequest.of(page, size, Sort.by("username").ascending()));
    }

    @Operation(summary = "Создать пользователя")
    @PostMapping("/create")
    public UserInfoDto createUser(
            @AuthenticationPrincipal User user,
            @RequestBody CreateUserRequest createUserRequest
    ) {
        return authService.createByRequest(user, createUserRequest);
    }

    @Operation(summary = "Сменить пароль")
    @PostMapping("/change-password")
    public void changePassword(
            @AuthenticationPrincipal User user,
            @RequestBody StringDto newPassword
    ) {
        authService.changePassword(user, newPassword);
    }
}
