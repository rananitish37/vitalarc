package com.vitalarc.user.service;

import com.vitalarc.user.dto.AuthResponse;
import com.vitalarc.user.dto.LoginRequest;
import com.vitalarc.user.dto.RegisterRequest;
import com.vitalarc.user.dto.UserResponse;
import com.vitalarc.user.entity.User;
import com.vitalarc.user.exception.EmailAlreadyExistsException;
import com.vitalarc.user.exception.InvalidCredentialsException;
import com.vitalarc.user.repository.UserRepository;
import com.vitalarc.user.security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Controllers stay thin - they parse the request and delegate here.
 * All business rules (uniqueness checks, password hashing, token issuance) live in this layer,
 * which makes them independently testable without spinning up the whole web context.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException(request.email());
        }

        User user = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.displayName()
        );
        userRepository.save(user);

        return buildAuthResponse(user);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return buildAuthResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        String token = jwtService.generateToken(user.getId(), user.getRole().name());
        return new AuthResponse(token, jwtService.getExpirationSeconds(), UserResponse.from(user));
    }
}
