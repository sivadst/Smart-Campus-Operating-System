package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.ChangePasswordRequest;
import com.campus.smartcampus.dto.request.UserUpdateRequest;
import com.campus.smartcampus.dto.response.UserResponse;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService Unit Tests")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .email("student@smartcampus.edu")
                .passwordHash("encodedHash")
                .firstName("Alice")
                .lastName("Walker")
                .phone("+1-555-0199")
                .role(UserRole.STUDENT)
                .isActive(true)
                .emailVerified(true)
                .build();
    }

    @Test
    @DisplayName("getAllUsers should return paginated UserResponses")
    void getAllUsers_ReturnsPagedResponses() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(user), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(page);

        Page<UserResponse> response = userService.getAllUsers(pageable);

        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getContent().get(0).getEmail()).isEqualTo("student@smartcampus.edu");
        assertThat(response.getContent().get(0).getFirstName()).isEqualTo("Alice");
        verify(userRepository, times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("getUserById should return user when found")
    void getUserById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserById(userId);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(userId);
        assertThat(response.getEmail()).isEqualTo("student@smartcampus.edu");
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUserById should throw ResourceNotFoundException when user missing")
    void getUserById_ThrowsNotFound_WhenIdDoesNotExist() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserById(userId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("getUserByEmail should return user when found")
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("student@smartcampus.edu")).thenReturn(Optional.of(user));

        UserResponse response = userService.getUserByEmail("student@smartcampus.edu");

        assertThat(response).isNotNull();
        assertThat(response.getEmail()).isEqualTo("student@smartcampus.edu");
        verify(userRepository, times(1)).findByEmail("student@smartcampus.edu");
    }

    @Test
    @DisplayName("getUserByEmail should throw ResourceNotFoundException when not found")
    void getUserByEmail_ThrowsNotFound_WhenEmailDoesNotExist() {
        when(userRepository.findByEmail("unknown@smartcampus.edu")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getUserByEmail("unknown@smartcampus.edu"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("User");

        verify(userRepository, times(1)).findByEmail("unknown@smartcampus.edu");
    }

    @Test
    @DisplayName("updateUser should update fields and return updated response")
    void updateUser_Success() {
        UserUpdateRequest updateReq = UserUpdateRequest.builder()
                .firstName("Alicia")
                .lastName("Smith")
                .phone("+1-555-0200")
                .role(UserRole.FACULTY)
                .isActive(true)
                .emailVerified(true)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse response = userService.updateUser(userId, updateReq);

        assertThat(response.getFirstName()).isEqualTo("Alicia");
        assertThat(response.getLastName()).isEqualTo("Smith");
        assertThat(response.getRole()).isEqualTo(UserRole.FACULTY);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("changePassword should encode and save new password when current matches")
    void changePassword_Success() {
        ChangePasswordRequest pwReq = ChangePasswordRequest.builder()
                .currentPassword("OldPass@123")
                .newPassword("NewPass@123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("OldPass@123", "encodedHash")).thenReturn(true);
        when(passwordEncoder.encode("NewPass@123")).thenReturn("newEncodedHash");
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.changePassword(userId, pwReq);

        assertThat(user.getPasswordHash()).isEqualTo("newEncodedHash");
        verify(userRepository, times(1)).save(user);
    }

    @Test
    @DisplayName("changePassword should throw BadRequestException when current password does not match")
    void changePassword_ThrowsBadRequest_WhenPasswordIncorrect() {
        ChangePasswordRequest pwReq = ChangePasswordRequest.builder()
                .currentPassword("WrongPass@123")
                .newPassword("NewPass@123")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("WrongPass@123", "encodedHash")).thenReturn(false);

        assertThatThrownBy(() -> userService.changePassword(userId, pwReq))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Current password is incorrect");

        verify(userRepository, never()).save(any());
    }

    @Test
    @DisplayName("deactivateUser should set isActive to false and save")
    void deactivateUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.deactivateUser(userId);

        assertThat(user.isActive()).isFalse();
        verify(userRepository, times(1)).save(user);
    }
}
