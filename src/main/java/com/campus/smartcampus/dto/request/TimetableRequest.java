package com.campus.smartcampus.dto.request;

import com.campus.smartcampus.enums.DayOfWeekEnum;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableRequest {
    @NotNull(message = "Course ID is required")
    private UUID courseId;

    @NotNull(message = "Room ID is required")
    private UUID roomId;

    @NotNull(message = "Faculty ID is required")
    private UUID facultyId;

    @NotNull(message = "Day of week is required")
    private DayOfWeekEnum dayOfWeek;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    @NotNull(message = "End time is required")
    private LocalTime endTime;

    @NotNull(message = "Academic year is required")
    private String academicYear;

    private int semester;
    private String section;
}
