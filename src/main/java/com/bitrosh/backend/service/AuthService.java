package com.bitrosh.backend.service;

import com.bitrosh.backend.cofiguration.DtoMapper;
import com.bitrosh.backend.dto.auth.JwtAuthDto;
import com.bitrosh.backend.dto.auth.SignInRequest;
import com.bitrosh.backend.dto.auth.SignUpRequest;
import com.bitrosh.backend.dto.core.WorkspaceResDto;
import com.bitrosh.backend.dao.entity.User;
import com.bitrosh.backend.exception.IncorrectLoginDataException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DtoMapper dtoMapper;

    @PostConstruct
    public void init() {
        if (userService.getAdminCount() == 0) {
            userService.create(User.builder().username("admin")
                    .password(passwordEncoder.encode("admin"))
                    .role(User.Role.ADMIN)
                    .build());
        }
    }

    public JwtAuthDto signUp(SignUpRequest request) {

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        userService.create(user);

        String jwt = jwtService.generateToken(user);
        return JwtAuthDto.builder()
                .token(jwt)
                .username(request.getUsername())
                .role(user.getRole().name())
                .currentWorkspace(dtoMapper.map(user.getCurrentWorkspace(), WorkspaceResDto.class))
                .build();
    }

    public JwtAuthDto signIn(SignInRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new IncorrectLoginDataException("Неверный логин или пароль");
        }

        User user = (User) userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);
        return JwtAuthDto.builder()
                .token(jwt)
                .username(request.getUsername())
                .role(user.getRole().name())
                .currentWorkspace(dtoMapper.map(user.getCurrentWorkspace(), WorkspaceResDto.class))
                .build();
    }
}
