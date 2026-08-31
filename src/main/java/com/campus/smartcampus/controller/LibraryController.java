package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.BookIssueRequest;
import com.campus.smartcampus.dto.request.BookRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.BookIssueResponse;
import com.campus.smartcampus.dto.response.BookResponse;
import com.campus.smartcampus.enums.BookCategory;
import com.campus.smartcampus.security.CustomUserDetails;
import com.campus.smartcampus.service.LibraryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/library")
@RequiredArgsConstructor
@Tag(name = "Library", description = "Library catalog and book circulation management")
public class LibraryController {

    private final LibraryService libraryService;

    @PostMapping("/books")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Add a new book to catalog")
    public ResponseEntity<ApiResponse<BookResponse>> addBook(@Valid @RequestBody BookRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(libraryService.addBook(request), "Book added to catalog"));
    }

    @GetMapping("/books")
    @Operation(summary = "List all books (paginated)")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getAllBooks(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(libraryService.getAllBooks(pageable)));
    }

    @GetMapping("/books/search")
    @Operation(summary = "Search books by title")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> searchBooks(@RequestParam String title, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(libraryService.searchBooksByTitle(title, pageable)));
    }

    @GetMapping("/books/category/{category}")
    @Operation(summary = "Get books by category")
    public ResponseEntity<ApiResponse<Page<BookResponse>>> getBooksByCategory(
            @PathVariable BookCategory category, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(libraryService.getBooksByCategory(category, pageable)));
    }

    @PostMapping("/issue")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Issue a book to a user")
    public ResponseEntity<ApiResponse<BookIssueResponse>> issueBook(@Valid @RequestBody BookIssueRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(libraryService.issueBook(request), "Book issued successfully"));
    }

    @PatchMapping("/return/{issueId}")
    @PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Return an issued book")
    public ResponseEntity<ApiResponse<BookIssueResponse>> returnBook(@PathVariable UUID issueId) {
        return ResponseEntity.ok(ApiResponse.success(libraryService.returnBook(issueId), "Book returned successfully"));
    }

    @GetMapping("/my-loans")
    @Operation(summary = "Get current user's book loan history")
    public ResponseEntity<ApiResponse<List<BookIssueResponse>>> getMyLoans(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                libraryService.getUserLoans(userDetails.getUser().getId())));
    }
}
