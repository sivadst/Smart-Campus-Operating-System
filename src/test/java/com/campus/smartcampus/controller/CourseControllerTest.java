package com.campus.smartcampus.controller;

import com.campus.smartcampus.dto.request.CourseRequest;
import com.campus.smartcampus.dto.response.CourseResponse;
import com.campus.smartcampus.enums.CourseStatus;
import com.campus.smartcampus.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
@DisplayName("CourseController Unit Tests")
class CourseControllerTest {

    @Mock
    private CourseService courseService;

    @InjectMocks
    private CourseController courseController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private UUID courseId;
    private UUID deptId;
    private CourseResponse courseResponse;
    private CourseRequest courseRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(courseController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
        objectMapper = new ObjectMapper();

        courseId = UUID.randomUUID();
        deptId = UUID.randomUUID();

        courseResponse = CourseResponse.builder()
                .id(courseId)
                .code("CS201")
                .name("Data Structures & Algorithms")
                .description("Fundamental computer science course")
                .credits(4)
                .semester(3)
                .departmentId(deptId)
                .departmentName("Computer Science")
                .status(CourseStatus.ACTIVE)
                .maxEnrollment(60)
                .currentEnrollment(45)
                .build();

        courseRequest = CourseRequest.builder()
                .code("CS201")
                .name("Data Structures & Algorithms")
                .description("Fundamental computer science course")
                .credits(4)
                .semester(3)
                .departmentId(deptId)
                .maxEnrollment(60)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/courses should create course and return 201 Created")
    void createCourse_ReturnsCreated() throws Exception {
        when(courseService.createCourse(any(CourseRequest.class))).thenReturn(courseResponse);

        mockMvc.perform(post("/api/v1/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Course created successfully"))
                .andExpect(jsonPath("$.data.code").value("CS201"))
                .andExpect(jsonPath("$.data.credits").value(4));

        verify(courseService).createCourse(any(CourseRequest.class));
    }

    @Test
    @DisplayName("GET /api/v1/courses should return paginated course list")
    void getAllCourses_ReturnsPage() throws Exception {
        Page<CourseResponse> page = new PageImpl<>(List.of(courseResponse));
        when(courseService.getAllCourses(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/v1/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.content[0].code").value("CS201"))
                .andExpect(jsonPath("$.data.content[0].name").value("Data Structures & Algorithms"));

        verify(courseService).getAllCourses(any(Pageable.class));
    }

    @Test
    @DisplayName("GET /api/v1/courses/{id} should return course by ID")
    void getCourseById_ReturnsCourse() throws Exception {
        when(courseService.getCourseById(courseId)).thenReturn(courseResponse);

        mockMvc.perform(get("/api/v1/courses/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.id").value(courseId.toString()))
                .andExpect(jsonPath("$.data.code").value("CS201"));

        verify(courseService).getCourseById(courseId);
    }

    @Test
    @DisplayName("GET /api/v1/courses/department/{departmentId} should return department courses")
    void getCoursesByDepartment_ReturnsList() throws Exception {
        when(courseService.getCoursesByDepartment(deptId)).thenReturn(List.of(courseResponse));

        mockMvc.perform(get("/api/v1/courses/department/{departmentId}", deptId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data[0].code").value("CS201"))
                .andExpect(jsonPath("$.data[0].departmentName").value("Computer Science"));

        verify(courseService).getCoursesByDepartment(deptId);
    }

    @Test
    @DisplayName("PUT /api/v1/courses/{id} should update course")
    void updateCourse_ReturnsUpdated() throws Exception {
        when(courseService.updateCourse(eq(courseId), any(CourseRequest.class))).thenReturn(courseResponse);

        mockMvc.perform(put("/api/v1/courses/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Course updated successfully"))
                .andExpect(jsonPath("$.data.code").value("CS201"));

        verify(courseService).updateCourse(eq(courseId), any(CourseRequest.class));
    }
}
