package com.campus.smartcampus.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueRequest {
    @NotNull(message = "Book ID is required")
    private UUID bookId;

    @NotNull(message = "User ID is required")
    private UUID userId;

    @Min(value = 1, message = "Issue days must be at least 1")
    @Builder.Default
    private int issueDays = 14;
}
