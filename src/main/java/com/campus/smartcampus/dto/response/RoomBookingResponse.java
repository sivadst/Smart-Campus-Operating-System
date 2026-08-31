package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.BookingStatus;
import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomBookingResponse {
    private UUID id;
    private UUID roomId;
    private String roomName;
    private String roomNumber;
    private String buildingName;
    private UUID bookedById;
    private String bookedByName;
    private String purpose;
    private Instant startTime;
    private Instant endTime;
    private BookingStatus status;
    private String approvedBy;
    private String remarks;
    private Integer attendeeCount;
    private Instant createdAt;
}
