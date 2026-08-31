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
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.repository.BookIssueRepository;
import com.campus.smartcampus.repository.BookRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("LibraryService Unit Tests")
class LibraryServiceTest {

    @Mock private BookRepository bookRepository;
    @Mock private BookIssueRepository bookIssueRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private LibraryService libraryService;

    private Book book;
    private User user;
    private UUID bookId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        userId = UUID.randomUUID();

        book = Book.builder()
                .id(bookId)
                .isbn("978-0134685991")
                .title("Effective Java")
                .author("Joshua Bloch")
                .category(BookCategory.COMPUTER_SCIENCE)
                .totalCopies(5)
                .availableCopies(3)
                .isActive(true)
                .build();

        user = User.builder()
                .id(userId)
                .email("reader@campus.edu")
                .firstName("John")
                .lastName("Doe")
                .role(UserRole.STUDENT)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should add new book to catalog successfully")
    void addBook_ValidRequest_ReturnsBookResponse() {
        BookRequest request = BookRequest.builder()
                .isbn("978-0134685991")
                .title("Effective Java")
                .author("Joshua Bloch")
                .category(BookCategory.COMPUTER_SCIENCE)
                .totalCopies(5)
                .build();

        when(bookRepository.existsByIsbn("978-0134685991")).thenReturn(false);
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponse response = libraryService.addBook(request);

        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo("Effective Java");
        assertThat(response.getAvailableCopies()).isEqualTo(3);
    }

    @Test
    @DisplayName("Should throw when adding book with duplicate ISBN")
    void addBook_DuplicateIsbn_ThrowsException() {
        BookRequest request = BookRequest.builder()
                .isbn("978-0134685991")
                .title("Effective Java")
                .author("Joshua Bloch")
                .category(BookCategory.COMPUTER_SCIENCE)
                .totalCopies(5)
                .build();

        when(bookRepository.existsByIsbn("978-0134685991")).thenReturn(true);

        assertThatThrownBy(() -> libraryService.addBook(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Should issue book successfully")
    void issueBook_ValidRequest_ReturnsIssueResponse() {
        BookIssueRequest request = BookIssueRequest.builder()
                .bookId(bookId)
                .userId(userId)
                .issueDays(14)
                .build();

        BookIssue issue = BookIssue.builder()
                .id(UUID.randomUUID())
                .book(book)
                .user(user)
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BookIssueStatus.ISSUED)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(bookIssueRepository.countByUserIdAndStatus(userId, BookIssueStatus.ISSUED)).thenReturn(1L);
        when(bookIssueRepository.save(any(BookIssue.class))).thenReturn(issue);

        BookIssueResponse response = libraryService.issueBook(request);

        assertThat(response).isNotNull();
        assertThat(response.getBookTitle()).isEqualTo("Effective Java");
        assertThat(response.getStatus()).isEqualTo(BookIssueStatus.ISSUED);
    }

    @Test
    @DisplayName("Should throw when issuing book with zero available copies")
    void issueBook_NoCopiesAvailable_ThrowsException() {
        book.setAvailableCopies(0);

        BookIssueRequest request = BookIssueRequest.builder()
                .bookId(bookId)
                .userId(userId)
                .build();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> libraryService.issueBook(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("No available copies");
    }
}
