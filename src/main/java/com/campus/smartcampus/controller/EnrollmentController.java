package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.EnrollmentRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.EnrollmentResponse;
import com.campus.smartcampus.service.EnrollmentService;
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
@RequestMapping("/api/v1/enrollments")
@RequiredArgsConstructor
@Tag(name = "Enrollments", description = "Student course enrollment management")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Enroll a student in a course")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> enrollStudent(@Valid @RequestBody EnrollmentRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(enrollmentService.enrollStudent(request), "Student enrolled successfully"));
    }

    @GetMapping("/student/{studentId}")
    @Operation(summary = "Get all active enrollments for a student")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getStudentEnrollments(@PathVariable UUID studentId) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getStudentEnrollments(studentId)));
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Get all enrolled students in a course")
    public ResponseEntity<ApiResponse<List<EnrollmentResponse>>> getCourseEnrollments(@PathVariable UUID courseId) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.getCourseEnrollments(courseId)));
    }

    @PatchMapping("/{id}/grade")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Update student grade for a course")
    public ResponseEntity<ApiResponse<EnrollmentResponse>> updateGrade(
            @PathVariable UUID id, @RequestParam String grade) {
        return ResponseEntity.ok(ApiResponse.success(enrollmentService.updateGrade(id, grade), "Grade updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Withdraw student from a course")
    public ResponseEntity<ApiResponse<Void>> withdrawEnrollment(@PathVariable UUID id) {
        enrollmentService.withdrawEnrollment(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Enrollment withdrawn successfully"));
    }
}
