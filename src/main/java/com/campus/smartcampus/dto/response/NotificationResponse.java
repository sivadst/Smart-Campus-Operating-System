package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.NotificationType;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponse {
    private UUID id;
    private String title;
    private String message;
    private NotificationType type;
    private boolean isRead;
    private Instant readAt;
    private String linkUrl;
    private Instant createdAt;
}
