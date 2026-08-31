package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.NoticeRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.NoticeResponse;
import com.campus.smartcampus.security.CustomUserDetails;
import com.campus.smartcampus.service.NoticeService;
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

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notices")
@RequiredArgsConstructor
@Tag(name = "Notices", description = "Campus notice and announcement management")
public class NoticeController {

    private final NoticeService noticeService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Create a new notice")
    public ResponseEntity<ApiResponse<NoticeResponse>> createNotice(
            @Valid @RequestBody NoticeRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        NoticeResponse response = noticeService.createNotice(request, userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(response, "Notice created successfully"));
    }

    @PutMapping("/{id}/publish")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Publish a notice")
    public ResponseEntity<ApiResponse<NoticeResponse>> publishNotice(@PathVariable UUID id) {
        NoticeResponse response = noticeService.publishNotice(id);
        return ResponseEntity.ok(ApiResponse.success(response, "Notice published successfully"));
    }

    @GetMapping
    @Operation(summary = "Get published notices with pagination")
    public ResponseEntity<ApiResponse<Page<NoticeResponse>>> getPublishedNotices(Pageable pageable) {
        Page<NoticeResponse> notices = noticeService.getPublishedNotices(pageable);
        return ResponseEntity.ok(ApiResponse.success(notices));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get notice by ID")
    public ResponseEntity<ApiResponse<NoticeResponse>> getNoticeById(@PathVariable UUID id) {
        NoticeResponse response = noticeService.getNoticeById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Delete a notice")
    public ResponseEntity<ApiResponse<Void>> deleteNotice(@PathVariable UUID id) {
        noticeService.deleteNotice(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Notice deleted successfully"));
    }
}
