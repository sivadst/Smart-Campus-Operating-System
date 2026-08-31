package com.campus.smartcampus.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingRequest {
    @NotNull(message = "Room ID is required")
    private UUID roomId;

    @NotBlank(message = "Purpose is required")
    private String purpose;

    @NotNull(message = "Start time is required")
    private Instant startTime;

    @NotNull(message = "End time is required")
    private Instant endTime;

    private String remarks;
    private Integer attendeeCount;
}
