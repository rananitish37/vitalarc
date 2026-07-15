package com.vitalarc.user.service;

import com.vitalarc.user.dto.LoginRequest;
import com.vitalarc.user.dto.RegisterRequest;
import com.vitalarc.user.entity.User;
import com.vitalarc.user.exception.EmailAlreadyExistsException;
import com.vitalarc.user.exception.InvalidCredentialsException;
import com.vitalarc.user.repository.UserRepository;
import com.vitalarc.user.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest("alex@example.com", "supersecret123", "Alex");
    }

    @Test
    void register_createsUserAndReturnsToken_whenEmailIsNew() {
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(false);
        when(passwordEncoder.encode(registerRequest.password())).thenReturn("hashed");
        when(jwtService.generateToken(any(), any())).thenReturn("fake-jwt");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        var response = authService.register(registerRequest);

        assertThat(response.accessToken()).isEqualTo("fake-jwt");
        assertThat(response.user().email()).isEqualTo("alex@example.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_throws_whenEmailAlreadyExists() {
        when(userRepository.existsByEmail(registerRequest.email())).thenReturn(true);

        assertThatThrownBy(() -> authService.register(registerRequest))
                .isInstanceOf(EmailAlreadyExistsException.class);

        verify(userRepository, never()).save(any());
    }

    @Test
    void login_throwsInvalidCredentials_whenPasswordDoesNotMatch() {
        User existingUser = new User("alex@example.com", "hashed", "Alex");
        LoginRequest loginRequest = new LoginRequest("alex@example.com", "wrong-password");

        when(userRepository.findByEmail("alex@example.com")).thenReturn(Optional.of(existingUser));
        when(passwordEncoder.matches("wrong-password", "hashed")).thenReturn(false);

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidCredentialsException.class);
    }

    @Test
    void login_throwsInvalidCredentials_whenEmailNotFound() {
        when(userRepository.findByEmail("ghost@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(new LoginRequest("ghost@example.com", "whatever")))
                .isInstanceOf(InvalidCredentialsException.class);
    }
}
