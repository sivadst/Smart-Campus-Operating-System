package com.campus.smartcampus.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentResponse {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private String headOfDepartment;
    private boolean isActive;
    private long courseCount;
}
