package com.bitrosh.backend.service;

import java.time.LocalDateTime;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.Role;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.entity.UserWorkspace;
import com.bitrosh.backend.dao.entity.Workspace;
import com.bitrosh.backend.dao.repository.RoleRepository;
import com.bitrosh.backend.dao.repository.UserRepository;
import com.bitrosh.backend.dao.repository.UserWorkspaceRepository;
import com.bitrosh.backend.dao.repository.WorkspaceRepository;
import com.bitrosh.backend.dto.core.WorkspaceReqDto;
import com.bitrosh.backend.dto.core.WorkspaceResDto;
import com.bitrosh.backend.dto.core.WorkspaceRoleDto;
import com.bitrosh.backend.exception.EntityNotFoundException;
import com.bitrosh.backend.exception.NoRulesException;
import com.bitrosh.backend.exception.UniqueValueExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final DtoMapper dtoMapper;

    public Page<WorkspaceResDto> getAllWorkspaces(User user, Pageable pageable) {
        return dtoMapper.map(
                user.isAdmin() ?
                    workspaceRepository.findAllWorkspaces(pageable) :
                    workspaceRepository.findAllWorkspacesByUserId(
                            user.getId(),
                            pageable
                    ),
                WorkspaceResDto.class
        );
    }

    @Transactional
    public WorkspaceResDto create(User user, WorkspaceReqDto workspace) {
        if (workspaceRepository.existsByName(workspace.getName())) {
            throw new UniqueValueExistsException("Рабочее пространство с таким именем уже существует");
        }
        Workspace entity = dtoMapper.map(workspace, Workspace.class);
        entity.setCreatedAt(LocalDateTime.now());
        entity = workspaceRepository.save(entity);
        userWorkspaceRepository.save(
                UserWorkspace.builder()
                    .workspace(entity)
                    .user(user)
                    .role(roleRepository.findByName(WorkspaceRoleDto.ADMIN.name()).orElseThrow())
                    .build());
        WorkspaceResDto dto = dtoMapper.map(entity, WorkspaceResDto.class);
        dto.setRole(WorkspaceRoleDto.ADMIN);
        return dto;
    }

    @Transactional
    public void delete(User user, String workspaceName) {
        Workspace workspace = workspaceRepository.findById(workspaceName).orElseThrow(
                () -> new EntityNotFoundException("Рабочее пространство с именем " + workspaceName + " не найдено")
        );
        if (!user.isAdmin() && !userWorkspaceRepository.existsByUserIdAndWorkspaceName(user.getId(), workspaceName)) {
            throw new NoRulesException("У вас нет прав на удаление рабочего пространства");
        }
        userWorkspaceRepository.deleteByWorkspaceName(workspaceName);
        workspaceRepository.delete(workspace);
    }

    public void inviteUser(User user, String workspaceName, String username, String roleName) {
        Workspace workspace = workspaceRepository.findById(workspaceName).orElseThrow(
                () -> new EntityNotFoundException("Рабочее пространство с именем " + workspaceName + " не найдено")
        );
        // моментик надо обсудить с правами на приглашения
        if (!user.isAdmin() && !userWorkspaceRepository.existsByUserIdAndWorkspaceName(user.getId(), workspaceName)) {
            throw new NoRulesException("У вас нет прав на добавление пользователей в это рабочее пространство");
        }
        User invitedUser = userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundException("Пользователь с именем " + username + " не найден")
        );
        Role role = roleRepository.findByName(roleName).orElseThrow(
                () -> new EntityNotFoundException("Роль с именем " + roleName + " не найдена")
        );
        userWorkspaceRepository.save(
                UserWorkspace.builder()
                    .workspace(workspace)
                    .user(invitedUser)
                    .role(role)
                    .build());
    }
}
