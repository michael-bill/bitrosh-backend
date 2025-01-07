package com.bitrosh.backend.service;

import com.bitrosh.backend.dto.auth.JwtAuthDto;
import com.bitrosh.backend.dto.auth.SignInRequest;
import com.bitrosh.backend.dto.auth.SignUpRequest;
import com.bitrosh.backend.entity.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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

        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.USER)
                .build();

        userService.create(user);

        var jwt = jwtService.generateToken(user);
        return new JwtAuthDto(jwt);
    }

    public JwtAuthDto signIn(SignInRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword())
        );
        User user = (User) userService
                .userDetailsService()
                .loadUserByUsername(request.getUsername());

        String jwt = jwtService.generateToken(user);
        return new JwtAuthDto(jwt);
    }
}
