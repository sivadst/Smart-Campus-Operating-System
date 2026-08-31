package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.TimetableRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.TimetableResponse;
import com.campus.smartcampus.enums.DayOfWeekEnum;
import com.campus.smartcampus.service.TimetableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/timetable")
@RequiredArgsConstructor
@Tag(name = "Timetable", description = "Class schedule management endpoints")
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Create a timetable entry")
    public ResponseEntity<ApiResponse<TimetableResponse>> createEntry(@Valid @RequestBody TimetableRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(timetableService.createEntry(request), "Timetable entry created"));
    }

    @GetMapping("/faculty/{facultyId}/day/{day}")
    @Operation(summary = "Get faculty schedule for a specific day")
    public ResponseEntity<ApiResponse<List<TimetableResponse>>> getFacultySchedule(
            @PathVariable UUID facultyId, @PathVariable DayOfWeekEnum day) {
        return ResponseEntity.ok(ApiResponse.success(timetableService.getFacultySchedule(facultyId, day)));
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get full schedule for a course")
    public ResponseEntity<ApiResponse<List<TimetableResponse>>> getCourseSchedule(@PathVariable UUID courseId) {
        return ResponseEntity.ok(ApiResponse.success(timetableService.getCourseSchedule(courseId)));
    }

    @GetMapping("/room/{roomId}/day/{day}")
    @Operation(summary = "Get room schedule for a specific day")
    public ResponseEntity<ApiResponse<List<TimetableResponse>>> getRoomSchedule(
            @PathVariable UUID roomId, @PathVariable DayOfWeekEnum day) {
        return ResponseEntity.ok(ApiResponse.success(timetableService.getRoomSchedule(roomId, day)));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Delete a timetable entry")
    public ResponseEntity<ApiResponse<Void>> deleteEntry(@PathVariable UUID id) {
        timetableService.deleteEntry(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Timetable entry deleted"));
    }
}
