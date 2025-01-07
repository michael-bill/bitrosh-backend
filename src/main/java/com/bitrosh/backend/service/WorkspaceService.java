package com.bitrosh.backend.service;

import java.time.LocalDateTime;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.Role;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.entity.UserWorkspace;
import com.bitrosh.backend.dao.entity.Workspace;
import com.bitrosh.backend.dao.repository.UserWorkspaceRepository;
import com.bitrosh.backend.dao.repository.WorkspaceRepository;
import com.bitrosh.backend.dto.core.WorkspaceReqDto;
import com.bitrosh.backend.dto.core.WorkspaceResDto;
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
        Workspace entity = dtoMapper.map(workspace, Workspace.class);
        entity.setCreatedAt(LocalDateTime.now());
        entity = workspaceRepository.save(entity);
        userWorkspaceRepository.save(UserWorkspace.builder()
                    .workspace(entity)
                    .user(user)
                    .build());
        return dtoMapper.map(entity, WorkspaceResDto.class);
    }
}
