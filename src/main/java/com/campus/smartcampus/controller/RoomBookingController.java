package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.RoomBookingRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.RoomBookingResponse;
import com.campus.smartcampus.security.CustomUserDetails;
import com.campus.smartcampus.service.RoomBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
@Tag(name = "Room Bookings", description = "Room reservation and approval management")
public class RoomBookingController {

    private final RoomBookingService bookingService;

    @PostMapping
    @Operation(summary = "Create a new room booking request")
    public ResponseEntity<ApiResponse<RoomBookingResponse>> createBooking(
            @Valid @RequestBody RoomBookingRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        bookingService.createBooking(request, userDetails.getUser().getId()),
                        "Booking request submitted successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get booking by ID")
    public ResponseEntity<ApiResponse<RoomBookingResponse>> getBookingById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getBookingById(id)));
    }

    @GetMapping("/my-bookings")
    @Operation(summary = "Get current user's bookings")
    public ResponseEntity<ApiResponse<List<RoomBookingResponse>>> getMyBookings(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.getUserBookings(userDetails.getUser().getId())));
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Approve a pending booking")
    public ResponseEntity<ApiResponse<RoomBookingResponse>> approveBooking(
            @PathVariable UUID id,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(ApiResponse.success(
                bookingService.approveBooking(id, userDetails.getUser().getEmail()),
                "Booking approved successfully"));
    }

    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Reject a pending booking")
    public ResponseEntity<ApiResponse<RoomBookingResponse>> rejectBooking(
            @PathVariable UUID id, @RequestParam(required = false) String remarks) {
        return ResponseEntity.ok(ApiResponse.success(
                bookingService.rejectBooking(id, remarks),
                "Booking rejected"));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<ApiResponse<RoomBookingResponse>> cancelBooking(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(bookingService.cancelBooking(id), "Booking cancelled"));
    }
}
