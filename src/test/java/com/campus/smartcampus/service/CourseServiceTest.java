package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.CourseRequest;
import com.campus.smartcampus.dto.response.CourseResponse;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.Department;
import com.campus.smartcampus.enums.CourseStatus;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.DepartmentRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Unit Tests")
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private UserRepository userRepository;

    @InjectMocks
    private CourseService courseService;

    private UUID departmentId;
    private Department department;
    private Course course;

    @BeforeEach
    void setUp() {
        departmentId = UUID.randomUUID();
        department = Department.builder()
                .id(departmentId)
                .name("Computer Science")
                .code("CSE")
                .build();

        course = Course.builder()
                .id(UUID.randomUUID())
                .code("CS101")
                .name("Data Structures")
                .credits(4)
                .semester(3)
                .department(department)
                .status(CourseStatus.ACTIVE)
                .maxEnrollment(60)
                .currentEnrollment(0)
                .build();
    }

    @Test
    @DisplayName("Should create course with valid department")
    void createCourse_ValidRequest_ReturnsCourseResponse() {
        CourseRequest request = CourseRequest.builder()
                .code("CS101").name("Data Structures").credits(4)
                .semester(3).departmentId(departmentId).maxEnrollment(60)
                .build();

        when(courseRepository.existsByCode("CS101")).thenReturn(false);
        when(departmentRepository.findById(departmentId)).thenReturn(Optional.of(department));
        when(courseRepository.save(any(Course.class))).thenReturn(course);

        CourseResponse response = courseService.createCourse(request);

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("CS101");
        assertThat(response.getDepartmentName()).isEqualTo("Computer Science");
    }

    @Test
    @DisplayName("Should throw when creating course with duplicate code")
    void createCourse_DuplicateCode_ThrowsException() {
        CourseRequest request = CourseRequest.builder()
                .code("CS101").name("Data Structures").credits(4)
                .semester(3).departmentId(departmentId)
                .build();

        when(courseRepository.existsByCode("CS101")).thenReturn(true);

        assertThatThrownBy(() -> courseService.createCourse(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Should throw when department not found")
    void createCourse_InvalidDepartment_ThrowsException() {
        CourseRequest request = CourseRequest.builder()
                .code("CS102").name("Algorithms").credits(3)
                .semester(4).departmentId(UUID.randomUUID())
                .build();

        when(courseRepository.existsByCode("CS102")).thenReturn(false);
        when(departmentRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> courseService.createCourse(request))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    @DisplayName("Should return courses by department")
    void getCoursesByDepartment_ReturnsList() {
        when(courseRepository.findAllByDepartmentId(departmentId)).thenReturn(List.of(course));

        List<CourseResponse> result = courseService.getCoursesByDepartment(departmentId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getDepartmentId()).isEqualTo(departmentId);
    }
}
