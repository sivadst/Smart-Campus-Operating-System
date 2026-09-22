package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.response.AttendanceResponse;
import com.campus.smartcampus.dto.response.AttendanceSummaryResponse;
import com.campus.smartcampus.enums.AttendanceStatus;
import com.campus.smartcampus.service.AttendanceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("AttendanceController Unit Tests")
class AttendanceControllerTest {

    @Mock
    private AttendanceService attendanceService;

    @InjectMocks
    private AttendanceController attendanceController;

    private MockMvc mockMvc;

    private UUID studentId;
    private UUID courseId;
    private AttendanceResponse attendanceResponse;
    private AttendanceSummaryResponse summaryResponse;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(attendanceController).build();

        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        attendanceResponse = AttendanceResponse.builder()
                .id(UUID.randomUUID())
                .studentId(studentId)
                .studentName("Divya Shanmugam")
                .courseId(courseId)
                .courseName("Operating Systems")
                .attendanceDate(LocalDate.of(2026, 10, 1))
                .status(AttendanceStatus.PRESENT)
                .markedByName("Prof. Karthik")
                .build();

        summaryResponse = AttendanceSummaryResponse.builder()
                .studentId(studentId)
                .studentName("Divya Shanmugam")
                .courseId(courseId)
                .courseName("Operating Systems")
                .totalClasses(30)
                .presentCount(27)
                .absentCount(2)
                .lateCount(1)
                .excusedCount(0)
                .attendancePercentage(90.0)
                .build();
    }

    @Test
    @DisplayName("GET /api/v1/attendance/course/{courseId}/date/{date} should return attendance records")
    void getAttendanceByCourseAndDate_ReturnsList() throws Exception {
        LocalDate date = LocalDate.of(2026, 10, 1);
        when(attendanceService.getAttendanceByCourseAndDate(courseId, date))
                .thenReturn(List.of(attendanceResponse));

        mockMvc.perform(get("/api/v1/attendance/course/{courseId}/date/{date}", courseId, "2026-10-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].studentName").value("Divya Shanmugam"))
                .andExpect(jsonPath("$.data[0].status").value("PRESENT"))
                .andExpect(jsonPath("$.data[0].markedByName").value("Prof. Karthik"));

        verify(attendanceService).getAttendanceByCourseAndDate(courseId, date);
    }

    @Test
    @DisplayName("GET /api/v1/attendance/student/{studentId}/course/{courseId} should return history")
    void getStudentAttendance_ReturnsList() throws Exception {
        when(attendanceService.getStudentAttendance(studentId, courseId))
                .thenReturn(List.of(attendanceResponse));

        mockMvc.perform(get("/api/v1/attendance/student/{studentId}/course/{courseId}", studentId, courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].courseName").value("Operating Systems"))
                .andExpect(jsonPath("$.data[0].status").value("PRESENT"));

        verify(attendanceService).getStudentAttendance(studentId, courseId);
    }

    @Test
    @DisplayName("GET /api/v1/attendance/summary/student/{studentId}/course/{courseId} should return summary")
    void getAttendanceSummary_ReturnsSummary() throws Exception {
        when(attendanceService.getAttendanceSummary(studentId, courseId)).thenReturn(summaryResponse);

        mockMvc.perform(get("/api/v1/attendance/summary/student/{studentId}/course/{courseId}",
                        studentId, courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.totalClasses").value(30))
                .andExpect(jsonPath("$.data.presentCount").value(27))
                .andExpect(jsonPath("$.data.absentCount").value(2))
                .andExpect(jsonPath("$.data.attendancePercentage").value(90.0));

        verify(attendanceService).getAttendanceSummary(studentId, courseId);
    }
}
