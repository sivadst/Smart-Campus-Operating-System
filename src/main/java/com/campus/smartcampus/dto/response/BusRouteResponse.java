package com.campus.smartcampus.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteResponse {
    private UUID id;
    private String routeNumber;
    private String source;
    private String destination;
    private String stops;
    private int totalSeats;
    private String driverName;
    private String driverPhone;
    private boolean isActive;
}
