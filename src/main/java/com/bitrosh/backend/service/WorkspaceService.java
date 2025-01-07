package com.bitrosh.backend.service;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.dao.repository.WorkspaceRepository;
import com.bitrosh.backend.dto.core.WorkspaceResDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final DtoMapper dtoMapper;

    public Page<WorkspaceResDto> getAllWorkspaces(User user, Pageable pageable) {
        return dtoMapper.map(
                workspaceRepository.findAllWorkspacesByUserId(
                        user.getId(),
                        pageable
                ),
                WorkspaceResDto.class
        );
    }
}
