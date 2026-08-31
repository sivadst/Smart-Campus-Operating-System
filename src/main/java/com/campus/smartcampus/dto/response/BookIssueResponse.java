package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.BookIssueStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookIssueResponse {
    private UUID id;
    private UUID bookId;
    private String bookTitle;
    private String bookIsbn;
    private UUID userId;
    private String userName;
    private String userEmail;
    private LocalDate issueDate;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BookIssueStatus status;
    private double fineAmount;
    private String remarks;
}
