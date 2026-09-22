package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.EnrollmentRequest;
import com.campus.smartcampus.dto.response.EnrollmentResponse;
import com.campus.smartcampus.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnrollmentController Unit Tests")
class EnrollmentControllerTest {

    @Mock
    private EnrollmentService enrollmentService;

    @InjectMocks
    private EnrollmentController enrollmentController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID enrollmentId;
    private UUID studentId;
    private UUID courseId;
    private EnrollmentResponse enrollmentResponse;
    private EnrollmentRequest enrollmentRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(enrollmentController).build();
        objectMapper = new ObjectMapper();

        enrollmentId = UUID.randomUUID();
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        enrollmentResponse = EnrollmentResponse.builder()
                .id(enrollmentId)
                .studentId(studentId)
                .studentName("Arun Kumar")
                .studentEmail("arun.kumar@smartcampus.edu")
                .courseId(courseId)
                .courseCode("CS301")
                .courseName("Database Management Systems")
                .enrolledAt(Instant.now())
                .isActive(true)
                .academicYear("2026-2027")
                .semester(5)
                .build();

        enrollmentRequest = EnrollmentRequest.builder()
                .studentId(studentId)
                .courseId(courseId)
                .academicYear("2026-2027")
                .semester(5)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/enrollments should enroll student and return 201 Created")
    void enrollStudent_ReturnsCreated() throws Exception {
        when(enrollmentService.enrollStudent(any(EnrollmentRequest.class))).thenReturn(enrollmentResponse);

        mockMvc.perform(post("/api/v1/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Student enrolled successfully"))
                .andExpect(jsonPath("$.data.courseCode").value("CS301"))
                .andExpect(jsonPath("$.data.studentName").value("Arun Kumar"));

        verify(enrollmentService).enrollStudent(any(EnrollmentRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/enrollments/student/{studentId} should return student enrollments")
    void getStudentEnrollments_ReturnsList() throws Exception {
        when(enrollmentService.getStudentEnrollments(studentId)).thenReturn(List.of(enrollmentResponse));

        mockMvc.perform(get("/api/v1/enrollments/student/{studentId}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].courseCode").value("CS301"))
                .andExpect(jsonPath("$.data[0].academicYear").value("2026-2027"))
                .andExpect(jsonPath("$.data[0].semester").value(5));

        verify(enrollmentService).getStudentEnrollments(studentId);
    }

    @Test
    @DisplayName("GET /api/v1/enrollments/course/{courseId} should return course enrollments")
    void getCourseEnrollments_ReturnsList() throws Exception {
        when(enrollmentService.getCourseEnrollments(courseId)).thenReturn(List.of(enrollmentResponse));

        mockMvc.perform(get("/api/v1/enrollments/course/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].studentEmail").value("arun.kumar@smartcampus.edu"))
                .andExpect(jsonPath("$.data[0].courseName").value("Database Management Systems"));

        verify(enrollmentService).getCourseEnrollments(courseId);
    }

    @Test
    @DisplayName("PATCH /api/v1/enrollments/{id}/grade should update grade and return 200 OK")
    void updateGrade_ReturnsUpdated() throws Exception {
        EnrollmentResponse gradedResponse = EnrollmentResponse.builder()
                .id(enrollmentId)
                .studentId(studentId)
                .studentName("Arun Kumar")
                .courseId(courseId)
                .courseCode("CS301")
                .courseName("Database Management Systems")
                .isActive(true)
                .grade("A+")
                .academicYear("2026-2027")
                .semester(5)
                .build();

        when(enrollmentService.updateGrade(eq(enrollmentId), eq("A+"))).thenReturn(gradedResponse);

        mockMvc.perform(patch("/api/v1/enrollments/{id}/grade", enrollmentId)
                        .param("grade", "A+"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Grade updated successfully"))
                .andExpect(jsonPath("$.data.grade").value("A+"));

        verify(enrollmentService).updateGrade(eq(enrollmentId), eq("A+"));
    }

    @Test
    @DisplayName("DELETE /api/v1/enrollments/{id} should withdraw enrollment and return 200 OK")
    void withdrawEnrollment_ReturnsOk() throws Exception {
        mockMvc.perform(delete("/api/v1/enrollments/{id}", enrollmentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Enrollment withdrawn successfully"));

        verify(enrollmentService).withdrawEnrollment(enrollmentId);
    }
}
