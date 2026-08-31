package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.CourseStatus;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {
    private UUID id;
    private String code;
    private String name;
    private String description;
    private int credits;
    private int semester;
    private UUID departmentId;
    private String departmentName;
    private UUID facultyId;
    private String facultyName;
    private CourseStatus status;
    private int maxEnrollment;
    private int currentEnrollment;
}
