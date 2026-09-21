package com.campus.smartcampus.service;

import com.campus.smartcampus.entity.Attendance;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.Department;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.AttendanceStatus;
import com.campus.smartcampus.enums.CourseStatus;
import com.campus.smartcampus.repository.AttendanceRepository;
import com.campus.smartcampus.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ExportService Unit Tests")
class ExportServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ExportService exportService;

    private Course course;
    private User student;
    private User faculty;
    private Department department;
    private Attendance attendance;
    private UUID courseId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();

        department = Department.builder()
                .id(UUID.randomUUID())
                .name("Computer Science")
                .code("CS")
                .build();

        student = User.builder()
                .id(UUID.randomUUID())
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@smartcampus.edu")
                .build();

        faculty = User.builder()
                .id(UUID.randomUUID())
                .firstName("Dr. Jane")
                .lastName("Smith")
                .email("jane.smith@smartcampus.edu")
                .build();

        course = Course.builder()
                .id(courseId)
                .code("CS101")
                .name("Intro to Programming")
                .department(department)
                .credits(4)
                .semester(1)
                .status(CourseStatus.ACTIVE)
                .maxEnrollment(50)
                .currentEnrollment(35)
                .build();

        attendance = Attendance.builder()
                .id(UUID.randomUUID())
                .student(student)
                .course(course)
                .attendanceDate(LocalDate.of(2026, 9, 21))
                .status(AttendanceStatus.PRESENT)
                .remarks("On time")
                .markedBy(faculty)
                .build();
    }

    @Test
    @DisplayName("exportCourseAttendanceCsv should generate valid CSV header and rows")
    void exportCourseAttendanceCsv_ReturnsValidCsvBytes() {
        LocalDate date = LocalDate.of(2026, 9, 21);
        when(attendanceRepository.findAllByCourseIdAndAttendanceDate(courseId, date))
                .thenReturn(List.of(attendance));

        byte[] csvBytes = exportService.exportCourseAttendanceCsv(courseId, date);
        String csvContent = new String(csvBytes, StandardCharsets.UTF_8);

        assertThat(csvBytes).isNotEmpty();
        assertThat(csvContent).contains("Student ID,Student Name,Email,Course Code,Course Name,Date,Status,Remarks,Marked By");
        assertThat(csvContent).contains("John Doe");
        assertThat(csvContent).contains("john.doe@smartcampus.edu");
        assertThat(csvContent).contains("CS101");
        assertThat(csvContent).contains("PRESENT");
        assertThat(csvContent).contains("On time");
        assertThat(csvContent).contains("jane.smith@smartcampus.edu");

        verify(attendanceRepository, times(1)).findAllByCourseIdAndAttendanceDate(courseId, date);
    }

    @Test
    @DisplayName("exportCourseAttendanceCsv should generate header only when no attendance records exist")
    void exportCourseAttendanceCsv_HandlesEmptyList() {
        LocalDate date = LocalDate.of(2026, 9, 22);
        when(attendanceRepository.findAllByCourseIdAndAttendanceDate(courseId, date))
                .thenReturn(Collections.emptyList());

        byte[] csvBytes = exportService.exportCourseAttendanceCsv(courseId, date);
        String csvContent = new String(csvBytes, StandardCharsets.UTF_8);

        assertThat(csvBytes).isNotEmpty();
        assertThat(csvContent).startsWith("Student ID,Student Name,Email,Course Code,Course Name,Date,Status,Remarks,Marked By");
        String[] lines = csvContent.split("\\R");
        assertThat(lines).hasSize(1);

        verify(attendanceRepository, times(1)).findAllByCourseIdAndAttendanceDate(courseId, date);
    }

    @Test
    @DisplayName("exportCourseCatalogCsv should generate valid CSV header and rows for all courses")
    void exportCourseCatalogCsv_ReturnsValidCsvBytes() {
        when(courseRepository.findAll()).thenReturn(List.of(course));

        byte[] csvBytes = exportService.exportCourseCatalogCsv();
        String csvContent = new String(csvBytes, StandardCharsets.UTF_8);

        assertThat(csvBytes).isNotEmpty();
        assertThat(csvContent).contains("Course Code,Course Name,Department,Credits,Semester,Status,Max Enrollment,Current Enrollment");
        assertThat(csvContent).contains("CS101");
        assertThat(csvContent).contains("Intro to Programming");
        assertThat(csvContent).contains("Computer Science");
        assertThat(csvContent).contains("4");
        assertThat(csvContent).contains("ACTIVE");
        assertThat(csvContent).contains("50");
        assertThat(csvContent).contains("35");

        verify(courseRepository, times(1)).findAll();
    }
}
