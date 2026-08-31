package com.campus.smartcampus.controller;

import com.campus.smartcampus.service.ExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/export")
@RequiredArgsConstructor
@Tag(name = "Export", description = "Data export and reporting services")
public class ExportController {

    private final ExportService exportService;

    @GetMapping("/attendance/course/{courseId}/date/{date}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Export course attendance records to CSV")
    public ResponseEntity<byte[]> exportAttendance(
            @PathVariable UUID courseId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        byte[] csv = exportService.exportCourseAttendanceCsv(courseId, date);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"attendance_" + date + ".csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }

    @GetMapping("/courses")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'FACULTY')")
    @Operation(summary = "Export entire course catalog to CSV")
    public ResponseEntity<byte[]> exportCourses() {
        byte[] csv = exportService.exportCourseCatalogCsv();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"course_catalog.csv\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(csv);
    }
}
