package com.bitrosh.backend.service;

import java.util.List;

import com.bitrosh.backend.dao.entity.Role;
import com.bitrosh.backend.dao.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private List<Role> roles;

    @PostConstruct
    public void init() {
        roles = roleRepository.findAll();
    }

    public Role getCachedByName(String name) {
        return roles.stream()
                .filter(role -> role.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Role not found"));
    }

    public boolean moreOrEqualsByName(String roleName1, String roleName2) {
        return getCachedByName(roleName1).getId() >= getCachedByName(roleName2).getId();
    }
}
