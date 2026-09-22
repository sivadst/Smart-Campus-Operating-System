package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.UserUpdateRequest;
import com.campus.smartcampus.dto.response.UserResponse;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserController Unit Tests")
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID userId;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();

        userId = UUID.randomUUID();

        userResponse = UserResponse.builder()
                .id(userId)
                .email("faculty.member@smartcampus.edu")
                .firstName("Meena")
                .lastName("Ramasamy")
                .phone("+91-9876543210")
                .role(UserRole.FACULTY)
                .isActive(true)
                .emailVerified(true)
                .lastLogin(Instant.now())
                .createdAt(Instant.now())
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/users should return paginated user list")
    void getAllUsers_ReturnsPage() throws Exception {
        Page<UserResponse> page = new PageImpl<>(List.of(userResponse));
        when(userService.getAllUsers(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].email").value("faculty.member@smartcampus.edu"))
                .andExpect(jsonPath("$.data.content[0].firstName").value("Meena"))
                .andExpect(jsonPath("$.data.content[0].role").value("FACULTY"));

        verify(userService).getAllUsers(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/users/{id} should return user by ID")
    void getUserById_ReturnsUser() throws Exception {
        when(userService.getUserById(userId)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(userId.toString()))
                .andExpect(jsonPath("$.data.lastName").value("Ramasamy"))
                .andExpect(jsonPath("$.data.phone").value("+91-9876543210"));

        verify(userService).getUserById(userId);
    }

    @Test
    @DisplayName("PUT /api/v1/users/{id} should update user profile and return 200 OK")
    void updateUser_ReturnsUpdated() throws Exception {
        UserUpdateRequest updateRequest = UserUpdateRequest.builder()
                .firstName("Meena")
                .lastName("Ramasamy-Pillai")
                .build();

        UserResponse updatedResponse = UserResponse.builder()
                .id(userId)
                .email("faculty.member@smartcampus.edu")
                .firstName("Meena")
                .lastName("Ramasamy-Pillai")
                .role(UserRole.FACULTY)
                .isActive(true)
                .build();

        when(userService.updateUser(eq(userId), any(UserUpdateRequest.class))).thenReturn(updatedResponse);

        mockMvc.perform(put("/api/v1/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User updated successfully"))
                .andExpect(jsonPath("$.data.lastName").value("Ramasamy-Pillai"));

        verify(userService).updateUser(eq(userId), any(UserUpdateRequest.class));
    }

    @Test
    @DisplayName("DELETE /api/v1/users/{id} should deactivate user and return 200 OK")
    void deactivateUser_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("User deactivated successfully"));

        verify(userService).deactivateUser(userId);
    }
}
