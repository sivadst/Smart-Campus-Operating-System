package com.campus.smartcampus.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceSummaryResponse {
    private UUID studentId;
    private String studentName;
    private UUID courseId;
    private String courseName;
    private long totalClasses;
    private long presentCount;
    private long absentCount;
    private long lateCount;
    private long excusedCount;
    private double attendancePercentage;
}
