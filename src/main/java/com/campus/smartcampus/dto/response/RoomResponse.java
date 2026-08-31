package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.RoomType;
import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
    private UUID id;
    private String roomNumber;
    private String name;
    private RoomType roomType;
    private int capacity;
    private int floor;
    private UUID buildingId;
    private String buildingName;
    private boolean hasProjector;
    private boolean hasAC;
    private boolean hasWifi;
    private boolean isAvailable;
}
