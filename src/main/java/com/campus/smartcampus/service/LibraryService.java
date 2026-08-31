package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.BookIssueRequest;
import com.campus.smartcampus.dto.request.BookRequest;
import com.campus.smartcampus.dto.response.BookIssueResponse;
import com.campus.smartcampus.dto.response.BookResponse;
import com.campus.smartcampus.entity.Book;
import com.campus.smartcampus.entity.BookIssue;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.BookCategory;
import com.campus.smartcampus.enums.BookIssueStatus;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.BookIssueRepository;
import com.campus.smartcampus.repository.BookRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LibraryService {

    private final BookRepository bookRepository;
    private final BookIssueRepository bookIssueRepository;
    private final UserRepository userRepository;

    private static final int MAX_BORROW_LIMIT = 5;
    private static final double DAILY_FINE_RATE = 2.0; // $2 per day overdue

    @Transactional
    public BookResponse addBook(BookRequest request) {
        if (bookRepository.existsByIsbn(request.getIsbn())) {
            throw new DuplicateResourceException("Book", "isbn", request.getIsbn());
        }

        Book book = Book.builder()
                .isbn(request.getIsbn())
                .title(request.getTitle())
                .author(request.getAuthor())
                .publisher(request.getPublisher())
                .publishedYear(request.getPublishedYear())
                .category(request.getCategory())
                .totalCopies(request.getTotalCopies())
                .availableCopies(request.getTotalCopies())
                .shelfLocation(request.getShelfLocation())
                .build();

        Book saved = bookRepository.save(book);
        log.info("Added book: {} (ISBN: {})", saved.getTitle(), saved.getIsbn());
        return mapToBookResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> getAllBooks(Pageable pageable) {
        return bookRepository.findAllByIsActiveTrue(pageable).map(this::mapToBookResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> searchBooksByTitle(String title, Pageable pageable) {
        return bookRepository.findAllByTitleContainingIgnoreCaseAndIsActiveTrue(title, pageable)
                .map(this::mapToBookResponse);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> getBooksByCategory(BookCategory category, Pageable pageable) {
        return bookRepository.findAllByCategoryAndIsActiveTrue(category, pageable)
                .map(this::mapToBookResponse);
    }

    @Transactional
    public BookIssueResponse issueBook(BookIssueRequest request) {
        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new ResourceNotFoundException("Book", "id", request.getBookId()));
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getUserId()));

        if (book.getAvailableCopies() <= 0) {
            throw new BadRequestException("No available copies for book: " + book.getTitle());
        }

        long activeLoans = bookIssueRepository.countByUserIdAndStatus(user.getId(), BookIssueStatus.ISSUED);
        if (activeLoans >= MAX_BORROW_LIMIT) {
            throw new BadRequestException("User has reached maximum borrowing limit of " + MAX_BORROW_LIMIT + " books");
        }

        LocalDate now = LocalDate.now();
        BookIssue issue = BookIssue.builder()
                .book(book)
                .user(user)
                .issueDate(now)
                .dueDate(now.plusDays(request.getIssueDays()))
                .status(BookIssueStatus.ISSUED)
                .build();

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);

        BookIssue saved = bookIssueRepository.save(issue);
        log.info("Issued book {} to user {}", book.getTitle(), user.getEmail());
        return mapToIssueResponse(saved);
    }

    @Transactional
    public BookIssueResponse returnBook(UUID issueId) {
        BookIssue issue = bookIssueRepository.findById(issueId)
                .orElseThrow(() -> new ResourceNotFoundException("BookIssue", "id", issueId));

        if (issue.getStatus() == BookIssueStatus.RETURNED) {
            throw new BadRequestException("Book has already been returned");
        }

        LocalDate today = LocalDate.now();
        issue.setReturnDate(today);
        issue.setStatus(BookIssueStatus.RETURNED);

        if (today.isAfter(issue.getDueDate())) {
            long daysOverdue = ChronoUnit.DAYS.between(issue.getDueDate(), today);
            issue.setFineAmount(daysOverdue * DAILY_FINE_RATE);
        }

        Book book = issue.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepository.save(book);

        BookIssue saved = bookIssueRepository.save(issue);
        log.info("Returned book {} with fine ${}", book.getTitle(), saved.getFineAmount());
        return mapToIssueResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BookIssueResponse> getUserLoans(UUID userId) {
        return bookIssueRepository.findAllByUserId(userId).stream()
                .map(this::mapToIssueResponse)
                .collect(Collectors.toList());
    }

    private BookResponse mapToBookResponse(Book book) {
        return BookResponse.builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .author(book.getAuthor())
                .publisher(book.getPublisher())
                .publishedYear(book.getPublishedYear())
                .category(book.getCategory())
                .totalCopies(book.getTotalCopies())
                .availableCopies(book.getAvailableCopies())
                .shelfLocation(book.getShelfLocation())
                .isActive(book.isActive())
                .build();
    }

    private BookIssueResponse mapToIssueResponse(BookIssue issue) {
        return BookIssueResponse.builder()
                .id(issue.getId())
                .bookId(issue.getBook().getId())
                .bookTitle(issue.getBook().getTitle())
                .bookIsbn(issue.getBook().getIsbn())
                .userId(issue.getUser().getId())
                .userName(issue.getUser().getFirstName() + " " + issue.getUser().getLastName())
                .userEmail(issue.getUser().getEmail())
                .issueDate(issue.getIssueDate())
                .dueDate(issue.getDueDate())
                .returnDate(issue.getReturnDate())
                .status(issue.getStatus())
                .fineAmount(issue.getFineAmount())
                .remarks(issue.getRemarks())
                .build();
    }
}
