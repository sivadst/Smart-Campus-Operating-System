package com.campus.smartcampus.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BuildingRequest {
    @NotBlank(message = "Building name is required")
    private String name;

    @NotBlank(message = "Building code is required")
    private String code;

    private String address;

    @Min(value = 1, message = "Total floors must be at least 1")
    private int totalFloors;

    private Double latitude;
    private Double longitude;
}
