package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.EventRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.EventResponse;
import com.campus.smartcampus.enums.EventCategory;
import com.campus.smartcampus.security.CustomUserDetails;
import com.campus.smartcampus.service.EventService;
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
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
@Tag(name = "Events", description = "Campus events, hackathons, and activities")
public class EventController {

    private final EventService eventService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Create a new campus event")
    public ResponseEntity<ApiResponse<EventResponse>> createEvent(
            @Valid @RequestBody EventRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        eventService.createEvent(request, userDetails.getUser().getId()),
                        "Event created successfully"));
    }

    @GetMapping
    @Operation(summary = "List all active events (paginated)")
    public ResponseEntity<ApiResponse<Page<EventResponse>>> getAllEvents(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getAllEvents(pageable)));
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming campus events")
    public ResponseEntity<ApiResponse<Page<EventResponse>>> getUpcomingEvents(Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getUpcomingEvents(pageable)));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get events by category")
    public ResponseEntity<ApiResponse<Page<EventResponse>>> getEventsByCategory(
            @PathVariable EventCategory category, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getEventsByCategory(category, pageable)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get event details by ID")
    public ResponseEntity<ApiResponse<EventResponse>> getEventById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(eventService.getEventById(id)));
    }

    @PostMapping("/{id}/rsvp")
    @Operation(summary = "RSVP / Register for an event")
    public ResponseEntity<ApiResponse<Void>> rsvpEvent(@PathVariable UUID id) {
        eventService.rsvpEvent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Successfully registered for event"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Cancel an event")
    public ResponseEntity<ApiResponse<Void>> cancelEvent(@PathVariable UUID id) {
        eventService.cancelEvent(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Event cancelled successfully"));
    }
}
