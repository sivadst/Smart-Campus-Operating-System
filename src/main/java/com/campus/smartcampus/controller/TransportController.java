package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.BusRouteRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.BusRouteResponse;
import com.campus.smartcampus.service.TransportService;
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
@RequestMapping("/api/v1/transport")
@RequiredArgsConstructor
@Tag(name = "Transport", description = "Campus bus fleet and route management")
public class TransportController {

    private final TransportService transportService;

    @PostMapping("/routes")
    @PreAuthorize("hasAnyRole('TRANSPORT_MANAGER', 'ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Create a new bus route")
    public ResponseEntity<ApiResponse<BusRouteResponse>> createRoute(@Valid @RequestBody BusRouteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(transportService.createRoute(request), "Bus route created successfully"));
    }

    @GetMapping("/routes")
    @Operation(summary = "List all active bus routes")
    public ResponseEntity<ApiResponse<List<BusRouteResponse>>> getAllRoutes() {
        return ResponseEntity.ok(ApiResponse.success(transportService.getAllActiveRoutes()));
    }

    @GetMapping("/routes/{id}")
    @Operation(summary = "Get bus route by ID")
    public ResponseEntity<ApiResponse<BusRouteResponse>> getRouteById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(transportService.getRouteById(id)));
    }

    @DeleteMapping("/routes/{id}")
    @PreAuthorize("hasAnyRole('TRANSPORT_MANAGER', 'ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Deactivate a bus route")
    public ResponseEntity<ApiResponse<Void>> deactivateRoute(@PathVariable UUID id) {
        transportService.deactivateRoute(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Bus route deactivated successfully"));
    }
}
