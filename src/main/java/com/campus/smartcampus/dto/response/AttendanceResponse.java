package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.AttendanceStatus;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceResponse {
    private UUID id;
    private UUID studentId;
    private String studentName;
    private UUID courseId;
    private String courseName;
    private LocalDate attendanceDate;
    private AttendanceStatus status;
    private String remarks;
    private String markedByName;
}
