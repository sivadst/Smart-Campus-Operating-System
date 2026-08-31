package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.BookIssue;
import com.campus.smartcampus.enums.BookIssueStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookIssueRepository extends JpaRepository<BookIssue, UUID> {
    List<BookIssue> findAllByUserId(UUID userId);
    List<BookIssue> findAllByUserIdAndStatus(UUID userId, BookIssueStatus status);
    List<BookIssue> findAllByBookId(UUID bookId);
    List<BookIssue> findAllByStatusAndDueDateBefore(BookIssueStatus status, LocalDate date);
    long countByUserIdAndStatus(UUID userId, BookIssueStatus status);
}
