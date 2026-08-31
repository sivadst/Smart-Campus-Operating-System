package com.campus.smartcampus.entity;

import com.campus.smartcampus.audit.Auditable;
import com.campus.smartcampus.enums.BookIssueStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "book_issues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookIssue extends Auditable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDate issueDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private LocalDate returnDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(nullable = false)
    private BookIssueStatus status = BookIssueStatus.ISSUED;

    @Builder.Default
    private double fineAmount = 0.0;

    private String remarks;
}
