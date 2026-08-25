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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Value("${spring.security.jwt.refresh-expiration}")
    private long refreshTokenDurationMs;

    @Transactional
    public AuthResponse registerUser(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .role(UserRole.STUDENT) // Default role for open registration, adjust based on requirements
                .isActive(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("Registered new user with email: {}", savedUser.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        RefreshToken refreshToken = createRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getTokenHash())
                .tokenType("Bearer")
                .build();
    }

    @Transactional
    public AuthResponse authenticateUser(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("User not found"));

        user.setLastLogin(Instant.now());
        userRepository.save(user);
        
        // Revoke all existing tokens (or create new based on policy)
        refreshTokenRepository.deleteByUser(user);
        RefreshToken refreshToken = createRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(jwt)
                .refreshToken(refreshToken.getTokenHash())
                .tokenType("Bearer")
                .build();
    }
    
    @Transactional
    public AuthResponse refreshToken(String requestRefreshToken) {
        return refreshTokenRepository.findByTokenHash(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    com.campus.smartcampus.security.CustomUserDetails userDetails = new com.campus.smartcampus.security.CustomUserDetails(user);
                    Authentication authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    
                    String token = tokenProvider.generateToken(authentication);
                    
                    return AuthResponse.builder()
                            .accessToken(token)
                            .refreshToken(requestRefreshToken)
                            .tokenType("Bearer")
                            .build();
                })
                .orElseThrow(() -> new UnauthorizedException("Refresh token is not in database or expired"));
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .tokenHash(UUID.randomUUID().toString()) // Using UUID as token for simplicity, can be stronger hash
                .expiresAt(Instant.now().plusMillis(refreshTokenDurationMs))
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiresAt().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new UnauthorizedException("Refresh token was expired. Please make a new signin request");
        }
        if (token.isRevoked()) {
            throw new UnauthorizedException("Refresh token was revoked");
        }
        return token;
    }
}
