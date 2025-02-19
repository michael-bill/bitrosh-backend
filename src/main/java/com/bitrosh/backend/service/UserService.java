package com.bitrosh.backend.service;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.UserWorkspace;
import com.bitrosh.backend.dao.repository.RoleRepository;
import com.bitrosh.backend.dao.repository.UserWorkspaceRepository;
import com.bitrosh.backend.dao.specification.UserSpecifications;
import com.bitrosh.backend.dto.core.MyUserInfoDto;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.UserInfoByWorkspaceDto;
import com.bitrosh.backend.dto.core.UserInfoDto;
import com.bitrosh.backend.dto.core.WorkspaceOrChatRoleDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.NoRulesException;
import com.bitrosh.backend.exception.UniqueValueExistsException;
import com.bitrosh.backend.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DtoMapper dtoMapper;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final WorkspaceService workspaceService;

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new UniqueValueExistsException("Пользователь с таким именем уже существует");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public long getAdminCount() {
        return userRepository.countByRole(User.Role.ADMIN);
    }

    public MyUserInfoDto getMyUserInfo(User user) {
        MyUserInfoDto dto = dtoMapper.map(user, MyUserInfoDto.class);
        dto.setUserId(user.getId());
        if (dto.getCurrentWorkspace() != null) {
            dto.getCurrentWorkspace().setWorkspaceRole(WorkspaceOrChatRoleDto.valueOf(roleRepository
                    .findByUserIdAndWorkspaceName(
                            user.getId(),
                            user.getCurrentWorkspace().getName()).orElseThrow(
                            () -> new EntityNotFoundException(
                                    "Не была найдена роль пользователя в текущем рабочем пространстве"
                            )
                    ).getName())
            );
        }
        return dto;
    }

    public Page<UserInfoByWorkspaceDto> getUserListByWorkspaceName(User user, String workspaceName, Pageable pageable) {
        if (workspaceService.hasNoRulesForWorkspace(user, workspaceName)) {
            throw new NoRulesException("У вас нет прав к этому рабочему пространству");
        }
        Page<UserWorkspace> page = userWorkspaceRepository.findByWorkspaceName(workspaceName, pageable);
        return new PageImpl<>(page.stream()
                .map(x -> UserInfoByWorkspaceDto.builder()
                        .id(x.getUser().getId())
                        .username(x.getUser().getUsername())
                        .role(x.getUser().getRole().name())
                        .workspaceRole(x.getRole().getName())
                        .build()).toList(),
                page.getPageable(),
                page.getTotalElements());
    }

    public Page<UserInfoDto> getAllUsers(String usernameKeyword, Pageable pageable) {
        return dtoMapper.map(
                userRepository.findAll(
                        UserSpecifications.usernameContains(usernameKeyword),
                        pageable),
                UserInfoDto.class
        );
    }

    @Deprecated
    public void getAdmin() {
        User user = getCurrentUser();
        user.setRole(User.Role.ADMIN);
        save(user);
    }
}
