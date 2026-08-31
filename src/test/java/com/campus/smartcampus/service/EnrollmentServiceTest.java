package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.EnrollmentRequest;
import com.campus.smartcampus.dto.response.EnrollmentResponse;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.Department;
import com.campus.smartcampus.entity.Enrollment;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.CourseStatus;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.EnrollmentRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("EnrollmentService Unit Tests")
class EnrollmentServiceTest {

    @Mock private EnrollmentRepository enrollmentRepository;
    @Mock private UserRepository userRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks
    private EnrollmentService enrollmentService;

    private UUID studentId, courseId;
    private User student;
    private Course course;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        courseId = UUID.randomUUID();

        student = User.builder()
                .id(studentId).email("student@campus.edu")
                .firstName("Jane").lastName("Doe")
                .role(UserRole.STUDENT).isActive(true)
                .passwordHash("hash")
                .build();

        Department dept = Department.builder()
                .id(UUID.randomUUID()).name("CS").code("CSE").build();

        course = Course.builder()
                .id(courseId).code("CS101").name("DSA")
                .department(dept).credits(4).semester(3)
                .status(CourseStatus.ACTIVE)
                .maxEnrollment(60).currentEnrollment(10)
                .build();
    }

    @Test
    @DisplayName("Should enroll student successfully")
    void enrollStudent_ValidRequest_ReturnsResponse() {
        EnrollmentRequest request = EnrollmentRequest.builder()
                .studentId(studentId).courseId(courseId)
                .academicYear("2026-2027").semester(1)
                .build();

        Enrollment enrollment = Enrollment.builder()
                .id(UUID.randomUUID()).student(student).course(course)
                .academicYear("2026-2027").semester(1)
                .enrolledAt(Instant.now()).isActive(true)
                .build();

        when(enrollmentRepository.existsByStudentIdAndCourseIdAndIsActiveTrue(studentId, courseId)).thenReturn(false);
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(enrollmentRepository.save(any())).thenReturn(enrollment);
        when(courseRepository.save(any())).thenReturn(course);

        EnrollmentResponse response = enrollmentService.enrollStudent(request);

        assertThat(response).isNotNull();
        assertThat(response.getStudentName()).isEqualTo("Jane Doe");
        assertThat(response.getCourseCode()).isEqualTo("CS101");
    }

    @Test
    @DisplayName("Should throw when capacity exceeded")
    void enrollStudent_CapacityFull_ThrowsException() {
        course.setCurrentEnrollment(60); // equals maxEnrollment

        EnrollmentRequest request = EnrollmentRequest.builder()
                .studentId(studentId).courseId(courseId)
                .academicYear("2026-2027").semester(1)
                .build();

        when(enrollmentRepository.existsByStudentIdAndCourseIdAndIsActiveTrue(studentId, courseId)).thenReturn(false);
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        assertThatThrownBy(() -> enrollmentService.enrollStudent(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("maximum enrollment capacity");
    }

    @Test
    @DisplayName("Should throw when already enrolled")
    void enrollStudent_AlreadyEnrolled_ThrowsException() {
        EnrollmentRequest request = EnrollmentRequest.builder()
                .studentId(studentId).courseId(courseId)
                .academicYear("2026-2027").semester(1)
                .build();

        when(enrollmentRepository.existsByStudentIdAndCourseIdAndIsActiveTrue(studentId, courseId)).thenReturn(true);

        assertThatThrownBy(() -> enrollmentService.enrollStudent(request))
                .isInstanceOf(DuplicateResourceException.class);
    }
}
