package com.campus.smartcampus.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusRouteRequest {
    @NotBlank(message = "Route number is required")
    private String routeNumber;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    private String stops;

    @Min(value = 1, message = "Total seats must be at least 1")
    private int totalSeats;

    private String driverName;
    private String driverPhone;
}
