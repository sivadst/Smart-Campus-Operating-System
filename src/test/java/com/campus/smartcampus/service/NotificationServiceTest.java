package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.response.NotificationResponse;
import com.campus.smartcampus.entity.Notification;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.NotificationType;
import com.campus.smartcampus.repository.NotificationRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationService Unit Tests")
class NotificationServiceTest {

    @Mock private NotificationRepository notificationRepository;
    @Mock private UserRepository userRepository;
    @InjectMocks private NotificationService notificationService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder().id(userId).email("student@campus.edu").build();
    }

    @Test
    @DisplayName("Should create and dispatch notification")
    void createNotification_ValidData_ReturnsNotificationResponse() {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID()).recipient(user).title("Test Alert").message("Testing notification dispatch")
                .type(NotificationType.SYSTEM).isRead(false)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        NotificationResponse response = notificationService.createNotification(
                userId, "Test Alert", "Testing notification dispatch", NotificationType.SYSTEM, null);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Test Alert");
        assertThat(response.isRead()).isFalse();
    }

    @Test
    @DisplayName("Should mark all notifications as read")
    void markAllAsRead_CallsRepositoryModifyingQuery() {
        notificationService.markAllAsRead(userId);
        verify(notificationRepository).markAllAsReadForUser(userId);
    }
}
