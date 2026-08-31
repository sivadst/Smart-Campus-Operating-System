package com.campus.smartcampus.dto.response;

import lombok.*;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingResponse {
    private UUID id;
    private String name;
    private String code;
    private String address;
    private int totalFloors;
    private Double latitude;
    private Double longitude;
    private boolean isActive;
    private long roomCount;
}
