package com.campus.smartcampus.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/public")
@Tag(name = "Health", description = "Application health and status endpoints")
public class HealthController {

    @GetMapping("/health")
    @Operation(summary = "Health check endpoint")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Smart Campus Operating System",
                "version", "1.0.0",
                "timestamp", Instant.now().toString()
        ));
    }

    @GetMapping("/info")
    @Operation(summary = "Application info endpoint")
    public ResponseEntity<Map<String, Object>> info() {
        return ResponseEntity.ok(Map.of(
                "application", "Smart Campus OS",
                "description", "Unified platform for smart campus management",
                "java", System.getProperty("java.version"),
                "modules", new String[]{
                        "Authentication & Authorization",
                        "Department Management",
                        "Course Management",
                        "Attendance Tracking",
                        "Room & Building Management",
                        "Timetable Scheduling",
                        "Notice Board",
                        "Room Booking"
                }
        ));
    }
}
