package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.BuildingRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.BuildingResponse;
import com.campus.smartcampus.service.BuildingService;
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
@RequestMapping("/api/v1/buildings")
@RequiredArgsConstructor
@Tag(name = "Buildings", description = "Campus building management endpoints")
public class BuildingController {

    private final BuildingService buildingService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Register a new campus building")
    public ResponseEntity<ApiResponse<BuildingResponse>> createBuilding(@Valid @RequestBody BuildingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(buildingService.createBuilding(request), "Building created successfully"));
    }

    @GetMapping
    @Operation(summary = "List all active buildings")
    public ResponseEntity<ApiResponse<List<BuildingResponse>>> getAllBuildings() {
        return ResponseEntity.ok(ApiResponse.success(buildingService.getAllBuildings()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get building by ID")
    public ResponseEntity<ApiResponse<BuildingResponse>> getBuildingById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(buildingService.getBuildingById(id)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Update building information")
    public ResponseEntity<ApiResponse<BuildingResponse>> updateBuilding(@PathVariable UUID id, @Valid @RequestBody BuildingRequest request) {
        return ResponseEntity.ok(ApiResponse.success(buildingService.updateBuilding(id, request), "Building updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @Operation(summary = "Soft-delete a building")
    public ResponseEntity<ApiResponse<Void>> deleteBuilding(@PathVariable UUID id) {
        buildingService.deleteBuilding(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Building deleted successfully"));
    }
}
