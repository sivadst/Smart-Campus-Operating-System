package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.DayOfWeekEnum;
import lombok.*;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TimetableResponse {
    private UUID id;
    private UUID courseId;
    private String courseCode;
    private String courseName;
    private UUID roomId;
    private String roomNumber;
    private String buildingName;
    private UUID facultyId;
    private String facultyName;
    private DayOfWeekEnum dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private String academicYear;
    private int semester;
    private String section;
}
