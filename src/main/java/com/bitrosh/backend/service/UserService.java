package com.bitrosh.backend.service;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.repository.RoleRepository;
import com.bitrosh.backend.dto.core.UserInfoDto;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dto.core.WorkspaceRoleDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.UniqueValueExistsException;
import com.bitrosh.backend.dao.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final RoleRepository roleRepository;
    private final DtoMapper dtoMapper;

    public User save(User user) {
        return repository.save(user);
    }

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UniqueValueExistsException("Пользователь с таким именем уже существует");
        }
        return save(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
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
        return repository.countByRole(User.Role.ADMIN);
    }

    public UserInfoDto getUserInfo(User user) {
        UserInfoDto dto = dtoMapper.map(user, UserInfoDto.class);
        if (dto.getCurrentWorkspace() != null) {
            dto.getCurrentWorkspace().setRole(WorkspaceRoleDto.valueOf(roleRepository
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

    @Deprecated
    public void getAdmin() {
        User user = getCurrentUser();
        user.setRole(User.Role.ADMIN);
        save(user);
    }
}
