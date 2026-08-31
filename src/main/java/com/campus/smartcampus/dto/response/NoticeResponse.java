package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.NoticeCategory;
import com.campus.smartcampus.enums.UserRole;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeResponse {
    private UUID id;
    private String title;
    private String content;
    private NoticeCategory category;
    private boolean isPinned;
    private boolean isPublished;
    private Instant publishedAt;
    private Instant expiresAt;
    private UUID authorId;
    private String authorName;
    private UserRole targetRole;
    private String attachmentUrl;
    private Instant createdAt;
}
