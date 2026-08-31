package com.campus.smartcampus.dto.request;

import com.campus.smartcampus.enums.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {
    @NotBlank(message = "Room number is required")
    private String roomNumber;

    @NotBlank(message = "Room name is required")
    private String name;

    @NotNull(message = "Room type is required")
    private RoomType roomType;

    @Min(value = 1, message = "Capacity must be at least 1")
    private int capacity;

    @Min(value = 0, message = "Floor must be non-negative")
    private int floor;

    @NotNull(message = "Building ID is required")
    private UUID buildingId;

    private boolean hasProjector;
    private boolean hasAC;
    private boolean hasWifi;
}
