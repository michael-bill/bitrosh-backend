package com.bitrosh.backend.service;

import com.bitrosh.backend.entity.User;
import com.bitrosh.backend.exception.UniqueValueExistsException;
import com.bitrosh.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

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
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    public long getAdminCount() {
        return repository.countByRole(User.Role.ADMIN);
    }

    @Deprecated
    public void getAdmin() {
        var user = getCurrentUser();
        user.setRole(User.Role.ADMIN);
        save(user);
    }
}
