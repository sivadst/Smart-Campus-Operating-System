package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.AttendanceRequest;
import com.campus.smartcampus.dto.response.AttendanceResponse;
import com.campus.smartcampus.dto.response.AttendanceSummaryResponse;
import com.campus.smartcampus.entity.Attendance;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.AttendanceStatus;
import com.campus.smartcampus.enums.UserRole;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.repository.AttendanceRepository;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AttendanceService Unit Tests")
class AttendanceServiceTest {

    @Mock private AttendanceRepository attendanceRepository;
    @Mock private UserRepository userRepository;
    @Mock private CourseRepository courseRepository;

    @InjectMocks private AttendanceService attendanceService;

    private User student, faculty;
    private Course course;
    private UUID studentId, facultyId, courseId;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        studentId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        courseId = UUID.randomUUID();
        today = LocalDate.now();

        student = User.builder().id(studentId).firstName("John").lastName("Doe").email("john@campus.edu").role(UserRole.STUDENT).build();
        faculty = User.builder().id(facultyId).firstName("Prof").lastName("Smith").email("smith@campus.edu").role(UserRole.FACULTY).build();
        course = Course.builder().id(courseId).code("CS201").name("Algorithms").build();
    }

    @Test
    @DisplayName("Should mark attendance successfully")
    void markAttendance_ValidRequest_ReturnsAttendanceResponse() {
        AttendanceRequest request = AttendanceRequest.builder()
                .studentId(studentId).courseId(courseId).attendanceDate(today).status(AttendanceStatus.PRESENT)
                .build();

        Attendance attendance = Attendance.builder()
                .id(UUID.randomUUID()).student(student).course(course).attendanceDate(today)
                .status(AttendanceStatus.PRESENT).markedBy(faculty)
                .build();

        when(attendanceRepository.existsByStudentIdAndCourseIdAndAttendanceDate(studentId, courseId, today)).thenReturn(false);
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(userRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(attendanceRepository.save(any(Attendance.class))).thenReturn(attendance);

        AttendanceResponse response = attendanceService.markAttendance(request, facultyId);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(AttendanceStatus.PRESENT);
        assertThat(response.getStudentName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on same day marking")
    void markAttendance_DuplicateDate_ThrowsException() {
        AttendanceRequest request = AttendanceRequest.builder()
                .studentId(studentId).courseId(courseId).attendanceDate(today).status(AttendanceStatus.PRESENT)
                .build();

        when(attendanceRepository.existsByStudentIdAndCourseIdAndAttendanceDate(studentId, courseId, today)).thenReturn(true);

        assertThatThrownBy(() -> attendanceService.markAttendance(request, facultyId))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Should calculate attendance percentage accurately")
    void getAttendanceSummary_ReturnsCalculatedPercentage() {
        when(userRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(attendanceRepository.countTotalByStudentIdAndCourseId(studentId, courseId)).thenReturn(20L);
        when(attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.PRESENT)).thenReturn(16L);
        when(attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.ABSENT)).thenReturn(2L);
        when(attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.LATE)).thenReturn(2L);
        when(attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.EXCUSED)).thenReturn(0L);

        AttendanceSummaryResponse summary = attendanceService.getAttendanceSummary(studentId, courseId);

        assertThat(summary).isNotNull();
        assertThat(summary.getTotalClasses()).isEqualTo(20L);
        assertThat(summary.getAttendancePercentage()).isEqualTo(90.0);
    }
}
