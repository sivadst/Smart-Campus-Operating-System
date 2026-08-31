package com.campus.smartcampus.dto.response;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private String studentEmail;
    private UUID courseId;
    private String courseCode;
    private String courseName;
    private Instant enrolledAt;
    private boolean isActive;
    private String grade;
    private String academicYear;
    private int semester;
}
