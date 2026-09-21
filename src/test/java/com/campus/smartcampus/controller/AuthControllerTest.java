package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.LoginRequest;
import com.campus.smartcampus.dto.request.RefreshTokenRequest;
import com.campus.smartcampus.dto.request.RegisterRequest;
import com.campus.smartcampus.dto.response.AuthResponse;
import com.campus.smartcampus.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthController Unit Tests")
class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
        objectMapper = new ObjectMapper();

        authResponse = AuthResponse.builder()
                .accessToken("sample-jwt-access-token")
                .refreshToken("sample-refresh-token")
                .tokenType("Bearer")
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/auth/register should return 201 Created with tokens")
    void registerUser_ReturnsCreated() throws Exception {
        RegisterRequest registerReq = RegisterRequest.builder()
                .email("newuser@smartcampus.edu")
                .password("StrongPass@123")
                .firstName("John")
                .lastName("Doe")
                .phone("+1-555-1234")
                .build();

        when(authService.registerUser(any(RegisterRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User registered successfully"))
                .andExpect(jsonPath("$.data.accessToken").value("sample-jwt-access-token"))
                .andExpect(jsonPath("$.data.refreshToken").value("sample-refresh-token"))
                .andExpect(jsonPath("$.data.tokenType").value("Bearer"));

        verify(authService).registerUser(any(RegisterRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login should return 200 OK with authenticated tokens")
    void authenticateUser_ReturnsOk() throws Exception {
        LoginRequest loginReq = LoginRequest.builder()
                .email("admin@smartcampus.edu")
                .password("Admin@123")
                .build();

        when(authService.authenticateUser(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User authenticated successfully"))
                .andExpect(jsonPath("$.data.accessToken").value("sample-jwt-access-token"));

        verify(authService).authenticateUser(any(LoginRequest.class));
    }

    @Test
    @DisplayName("POST /api/v1/auth/refresh should return 200 OK with refreshed tokens")
    void refreshToken_ReturnsOk() throws Exception {
        RefreshTokenRequest refreshReq = RefreshTokenRequest.builder()
                .refreshToken("sample-refresh-token")
                .build();

        when(authService.refreshToken("sample-refresh-token")).thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Token refreshed successfully"))
                .andExpect(jsonPath("$.data.accessToken").value("sample-jwt-access-token"));

        verify(authService).refreshToken("sample-refresh-token");
    }
}
