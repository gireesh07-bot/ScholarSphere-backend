package com.scholarsphere.auth.services;

import com.scholarsphere.auth.dto.LoginRequest;
import com.scholarsphere.auth.dto.LoginResponse;
import com.scholarsphere.auth.dto.RegisterRequest;
import com.scholarsphere.auth.entity.User;
import com.scholarsphere.auth.repository.UserRepository;
import com.scholarsphere.auth.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // ==========================
    // REGISTER
    // ==========================

    public User register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole("USER");
        user.setStatus("ACTIVE");

        return userRepository.save(user);
    }


    // ==========================
    // LOGIN
    // ==========================

    public LoginResponse login(LoginRequest request) {

        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Invalid email or password"
                        )
                );

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPasswordHash()
                );

        if (!passwordMatches) {
            throw new RuntimeException(
                    "Invalid email or password"
            );
        }

        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException(
                    "User account is not active"
            );
        }

        String token = jwtService.generateToken(
                user.getUserId(),
                user.getEmail(),
                user.getRole()
        );

        return new LoginResponse(
                "Login successful",
                token
        );
    }
}