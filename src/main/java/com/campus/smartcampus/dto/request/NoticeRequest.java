package com.campus.smartcampus.dto.request;

import com.campus.smartcampus.enums.NoticeCategory;
import com.campus.smartcampus.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequest {
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private NoticeCategory category;
    private boolean isPinned;
    private Instant expiresAt;
    private UserRole targetRole;
    private String attachmentUrl;
}
