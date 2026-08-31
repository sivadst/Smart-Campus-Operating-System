package com.campus.smartcampus.dto.request;

import com.campus.smartcampus.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BulkAttendanceRequest {
    @NotNull(message = "Course ID is required")
    private UUID courseId;

    @NotNull(message = "Date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Attendance records are required")
    private List<StudentAttendance> records;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StudentAttendance {
        @NotNull(message = "Student ID is required")
        private UUID studentId;

        @NotNull(message = "Status is required")
        private AttendanceStatus status;

        private String remarks;
    }
}
