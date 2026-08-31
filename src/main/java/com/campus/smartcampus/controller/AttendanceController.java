package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.AttendanceRequest;
import com.campus.smartcampus.dto.request.BulkAttendanceRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.AttendanceResponse;
import com.campus.smartcampus.dto.response.AttendanceSummaryResponse;
import com.campus.smartcampus.security.CustomUserDetails;
import com.campus.smartcampus.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/attendance")
@RequiredArgsConstructor
@Tag(name = "Attendance", description = "Student attendance tracking and analytics")
public class AttendanceController {

    private final AttendanceService attendanceService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Mark attendance for a single student")
    public ResponseEntity<ApiResponse<AttendanceResponse>> markAttendance(
            @Valid @RequestBody AttendanceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        attendanceService.markAttendance(request, userDetails.getUser().getId()),
                        "Attendance marked successfully"));
    }

    @PostMapping("/bulk")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Mark attendance for multiple students at once")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> markBulkAttendance(
            @Valid @RequestBody BulkAttendanceRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        attendanceService.markBulkAttendance(request, userDetails.getUser().getId()),
                        "Bulk attendance marked successfully"));
    }

    @GetMapping("/course/{courseId}/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Get attendance by course and date")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getAttendanceByCourseAndDate(
            @PathVariable UUID courseId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceByCourseAndDate(courseId, date)));
    }

    @GetMapping("/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get student's attendance history for a course")
    public ResponseEntity<ApiResponse<List<AttendanceResponse>>> getStudentAttendance(
            @PathVariable UUID studentId, @PathVariable UUID courseId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getStudentAttendance(studentId, courseId)));
    }

    @GetMapping("/summary/student/{studentId}/course/{courseId}")
    @Operation(summary = "Get attendance summary with percentage")
    public ResponseEntity<ApiResponse<AttendanceSummaryResponse>> getAttendanceSummary(
            @PathVariable UUID studentId, @PathVariable UUID courseId) {
        return ResponseEntity.ok(ApiResponse.success(attendanceService.getAttendanceSummary(studentId, courseId)));
    }
}
