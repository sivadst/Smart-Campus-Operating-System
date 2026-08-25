package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.LoginRequest;
import com.campus.smartcampus.dto.request.RegisterRequest;
import com.campus.smartcampus.dto.response.AuthResponse;
import com.campus.smartcampus.entity.RefreshToken;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.UnauthorizedException;
import com.campus.smartcampus.repository.RefreshTokenRepository;
import com.campus.smartcampus.repository.UserRepository;
import com.campus.smartcampus.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenProvider tokenProvider;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(authService, "refreshTokenDurationMs", 604800000L);
    }

    @Test
    void registerUser_shouldReturnAuthResponse_whenValidRequest() {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "Password123!", "Test", "User", "1234567890");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        
        User savedUser = User.builder()
                .id(UUID.randomUUID())
                .email(request.getEmail())
                .passwordHash("encodedPassword")
                .role(UserRole.STUDENT)
                .build();
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("jwt-token");
        
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        AuthResponse response = authService.registerUser(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getRefreshToken()).isNotNull();
        assertThat(response.getTokenType()).isEqualTo("Bearer");
        
        verify(userRepository, times(1)).save(any(User.class));
        verify(refreshTokenRepository, times(1)).save(any(RefreshToken.class));
    }

    @Test
    void registerUser_shouldThrowException_whenEmailExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest("test@example.com", "Password123!", "Test", "User", "1234567890");
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> authService.registerUser(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUser_shouldReturnAuthResponse_whenValidCredentials() {
        // Arrange
        LoginRequest request = new LoginRequest("test@example.com", "Password123!");
        
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);
        when(tokenProvider.generateToken(auth)).thenReturn("jwt-token");
        
        User user = User.builder().id(UUID.randomUUID()).email("test@example.com").build();
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(user));
        
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        AuthResponse response = authService.authenticateUser(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isEqualTo("jwt-token");
        assertThat(response.getRefreshToken()).isNotNull();
        
        verify(userRepository, times(1)).save(user);
        verify(refreshTokenRepository, times(1)).deleteByUser(user);
    }
}
