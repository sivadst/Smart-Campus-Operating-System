package com.campus.smartcampus.security;

import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("JwtTokenProvider Unit Tests")
class JwtTokenProviderTest {

    private JwtTokenProvider tokenProvider;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        tokenProvider = new JwtTokenProvider();
        ReflectionTestUtils.setField(tokenProvider, "jwtSecret",
                "dGVzdC1zZWNyZXQta2V5LWZvci1qd3QtdG9rZW4tZ2VuZXJhdGlvbi1pbi10ZXN0LWVudmlyb25tZW50");
        ReflectionTestUtils.setField(tokenProvider, "jwtExpirationInMs", 900000L);

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("test@campus.edu")
                .passwordHash("hashedPassword")
                .firstName("Test")
                .lastName("User")
                .role(UserRole.STUDENT)
                .isActive(true)
                .build();

        CustomUserDetails userDetails = new CustomUserDetails(user);
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Test
    @DisplayName("Should generate a valid JWT token")
    void generateToken_ValidAuth_ReturnsToken() {
        String token = tokenProvider.generateToken(authentication);

        assertThat(token).isNotBlank();
        assertThat(token.split("\\.")).hasSize(3); // Header.Payload.Signature
    }

    @Test
    @DisplayName("Should extract user ID from JWT token")
    void getUserIdFromJWT_ValidToken_ReturnsUserId() {
        String token = tokenProvider.generateToken(authentication);
        String userId = tokenProvider.getUserIdFromJWT(token);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        assertThat(userId).isEqualTo(userDetails.getUser().getId().toString());
    }

    @Test
    @DisplayName("Should validate a valid token")
    void validateToken_ValidToken_ReturnsTrue() {
        String token = tokenProvider.generateToken(authentication);
        assertThat(tokenProvider.validateToken(token)).isTrue();
    }

    @Test
    @DisplayName("Should reject an invalid token")
    void validateToken_InvalidToken_ReturnsFalse() {
        assertThat(tokenProvider.validateToken("invalid.token.here")).isFalse();
    }

    @Test
    @DisplayName("Should reject an empty token")
    void validateToken_EmptyToken_ReturnsFalse() {
        assertThat(tokenProvider.validateToken("")).isFalse();
    }
}
