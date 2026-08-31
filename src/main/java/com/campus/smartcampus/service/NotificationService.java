package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.response.NotificationResponse;
import com.campus.smartcampus.entity.Notification;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.NotificationType;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.NotificationRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @Transactional
    public NotificationResponse createNotification(UUID recipientId, String title, String message,
                                                   NotificationType type, String linkUrl) {
        User recipient = userRepository.findById(recipientId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", recipientId));

        Notification notification = Notification.builder()
                .recipient(recipient)
                .title(title)
                .message(message)
                .type(type)
                .linkUrl(linkUrl)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Notification sent to {}: {}", recipient.getEmail(), title);
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<NotificationResponse> getUserNotifications(UUID recipientId, Pageable pageable) {
        return notificationRepository.findAllByRecipientIdOrderByCreatedAtDesc(recipientId, pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public long getUnreadCount(UUID recipientId) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(recipientId);
    }

    @Transactional
    public void markAsRead(UUID notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification", "id", notificationId));
        notification.setRead(true);
        notification.setReadAt(Instant.now());
        notificationRepository.save(notification);
    }

    @Transactional
    public void markAllAsRead(UUID recipientId) {
        notificationRepository.markAllAsReadForUser(recipientId);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .title(notification.getTitle())
                .message(notification.getMessage())
                .type(notification.getType())
                .isRead(notification.isRead())
                .readAt(notification.getReadAt())
                .linkUrl(notification.getLinkUrl())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
