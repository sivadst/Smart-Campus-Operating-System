package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.BookIssueRequest;
import com.campus.smartcampus.dto.request.BookRequest;
import com.campus.smartcampus.dto.response.BookIssueResponse;
import com.campus.smartcampus.dto.response.BookResponse;
import com.campus.smartcampus.enums.BookCategory;
import com.campus.smartcampus.enums.BookIssueStatus;
import com.campus.smartcampus.service.LibraryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("LibraryController Unit Tests")
class LibraryControllerTest {

    @Mock
    private LibraryService libraryService;

    @InjectMocks
    private LibraryController libraryController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID bookId;
    private UUID userId;
    private UUID issueId;
    private BookResponse bookResponse;
    private BookRequest bookRequest;
    private BookIssueResponse issueResponse;
    private BookIssueRequest issueRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(libraryController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();

        bookId = UUID.randomUUID();
        userId = UUID.randomUUID();
        issueId = UUID.randomUUID();

        bookResponse = BookResponse.builder()
                .id(bookId)
                .isbn("978-0134494166")
                .title("Clean Architecture")
                .author("Robert C. Martin")
                .publisher("Prentice Hall")
                .publishedYear(2017)
                .category(BookCategory.COMPUTER_SCIENCE)
                .totalCopies(5)
                .availableCopies(4)
                .shelfLocation("CS-02-B")
                .isActive(true)
                .build();

        bookRequest = BookRequest.builder()
                .isbn("978-0134494166")
                .title("Clean Architecture")
                .author("Robert C. Martin")
                .publisher("Prentice Hall")
                .publishedYear(2017)
                .category(BookCategory.COMPUTER_SCIENCE)
                .totalCopies(5)
                .shelfLocation("CS-02-B")
                .build();

        issueResponse = BookIssueResponse.builder()
                .id(issueId)
                .bookId(bookId)
                .bookTitle("Clean Architecture")
                .bookIsbn("978-0134494166")
                .userId(userId)
                .userName("John Doe")
                .userEmail("john.doe@smartcampus.edu")
                .issueDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(14))
                .status(BookIssueStatus.ISSUED)
                .fineAmount(0.0)
                .build();

        issueRequest = BookIssueRequest.builder()
                .bookId(bookId)
                .userId(userId)
                .issueDays(14)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/library/books should add book to catalog and return 201 Created")
    void addBook_ReturnsCreated() throws Exception {
        when(libraryService.addBook(any(BookRequest.class))).thenReturn(bookResponse);

        mockMvc.perform(post("/api/v1/library/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book added to catalog"))
                .andExpect(jsonPath("$.data.isbn").value("978-0134494166"))
                .andExpect(jsonPath("$.data.title").value("Clean Architecture"));

        verify(libraryService).addBook(any(BookRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/library/books should return paginated books")
    void getAllBooks_ReturnsPage() throws Exception {
        Page<BookResponse> page = new PageImpl<>(List.of(bookResponse));
        when(libraryService.getAllBooks(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/library/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].title").value("Clean Architecture"))
                .andExpect(jsonPath("$.data.content[0].author").value("Robert C. Martin"));

        verify(libraryService).getAllBooks(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/library/books/search should return books matching title")
    void searchBooks_ReturnsPage() throws Exception {
        Page<BookResponse> page = new PageImpl<>(List.of(bookResponse));
        when(libraryService.searchBooksByTitle(eq("Clean"), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/library/books/search")
                        .param("title", "Clean"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].isbn").value("978-0134494166"));

        verify(libraryService).searchBooksByTitle(eq("Clean"), any(Pageable.class));
    }

    @Test
    @DisplayName("POST /api/v1/library/issue should issue book and return 201 Created")
    void issueBook_ReturnsCreated() throws Exception {
        when(libraryService.issueBook(any(BookIssueRequest.class))).thenReturn(issueResponse);

        mockMvc.perform(post("/api/v1/library/issue")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(issueRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book issued successfully"))
                .andExpect(jsonPath("$.data.bookTitle").value("Clean Architecture"))
                .andExpect(jsonPath("$.data.status").value("ISSUED"));

        verify(libraryService).issueBook(any(BookIssueRequest.class));
    }

    @Test
    @DisplayName("PATCH /api/v1/library/return/{issueId} should mark book as returned")
    void returnBook_ReturnsUpdated() throws Exception {
        BookIssueResponse returnedResponse = BookIssueResponse.builder()
                .id(issueId)
                .bookId(bookId)
                .bookTitle("Clean Architecture")
                .userId(userId)
                .status(BookIssueStatus.RETURNED)
                .returnDate(LocalDate.now())
                .fineAmount(0.0)
                .build();

        when(libraryService.returnBook(issueId)).thenReturn(returnedResponse);

        mockMvc.perform(patch("/api/v1/library/return/{issueId}", issueId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Book returned successfully"))
                .andExpect(jsonPath("$.data.status").value("RETURNED"));

        verify(libraryService).returnBook(issueId);
    }
}
