package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.RoomRequest;
import com.campus.smartcampus.dto.response.ApiResponse;
import com.campus.smartcampus.dto.response.RoomResponse;
import com.campus.smartcampus.enums.RoomType;
import com.campus.smartcampus.service.RoomService;
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
@RequestMapping("/api/v1/rooms")
@RequiredArgsConstructor
@Tag(name = "Rooms", description = "Campus room management and availability endpoints")
public class RoomController {

    private final RoomService roomService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Add a new room to a building")
    public ResponseEntity<ApiResponse<RoomResponse>> createRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(roomService.createRoom(request), "Room created successfully"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get room by ID")
    public ResponseEntity<ApiResponse<RoomResponse>> getRoomById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getRoomById(id)));
    }

    @GetMapping("/building/{buildingId}")
    @Operation(summary = "Get all rooms in a building")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getRoomsByBuilding(@PathVariable UUID buildingId) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getRoomsByBuilding(buildingId)));
    }

    @GetMapping("/available")
    @Operation(summary = "Get all available rooms")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getAvailableRooms() {
        return ResponseEntity.ok(ApiResponse.success(roomService.getAvailableRooms()));
    }

    @GetMapping("/type/{roomType}")
    @Operation(summary = "Get rooms by type")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getRoomsByType(@PathVariable RoomType roomType) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getRoomsByType(roomType)));
    }

    @GetMapping("/capacity/{minCapacity}")
    @Operation(summary = "Get rooms with minimum capacity")
    public ResponseEntity<ApiResponse<List<RoomResponse>>> getRoomsByCapacity(@PathVariable int minCapacity) {
        return ResponseEntity.ok(ApiResponse.success(roomService.getRoomsByCapacity(minCapacity)));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Update room details")
    public ResponseEntity<ApiResponse<RoomResponse>> updateRoom(@PathVariable UUID id, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(ApiResponse.success(roomService.updateRoom(id, request), "Room updated successfully"));
    }

    @PatchMapping("/{id}/toggle-availability")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Operation(summary = "Toggle room availability")
    public ResponseEntity<ApiResponse<Void>> toggleAvailability(@PathVariable UUID id) {
        roomService.toggleAvailability(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Room availability toggled"));
    }
}
