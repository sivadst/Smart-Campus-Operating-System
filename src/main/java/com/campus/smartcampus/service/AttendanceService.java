package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.AttendanceRequest;
import com.campus.smartcampus.dto.request.BulkAttendanceRequest;
import com.campus.smartcampus.dto.response.AttendanceResponse;
import com.campus.smartcampus.dto.response.AttendanceSummaryResponse;
import com.campus.smartcampus.entity.Attendance;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.AttendanceStatus;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.AttendanceRepository;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public AttendanceResponse markAttendance(AttendanceRequest request, UUID markedById) {
        if (attendanceRepository.existsByStudentIdAndCourseIdAndAttendanceDate(
                request.getStudentId(), request.getCourseId(), request.getAttendanceDate())) {
            throw new DuplicateResourceException("Attendance", "student+course+date",
                    request.getStudentId() + "/" + request.getCourseId() + "/" + request.getAttendanceDate());
        }

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));
        User markedBy = userRepository.findById(markedById)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", markedById));

        Attendance attendance = Attendance.builder()
                .student(student)
                .course(course)
                .attendanceDate(request.getAttendanceDate())
                .status(request.getStatus())
                .remarks(request.getRemarks())
                .markedBy(markedBy)
                .build();

        Attendance saved = attendanceRepository.save(attendance);
        log.info("Marked attendance for student {} in course {} on {}",
                student.getEmail(), course.getCode(), request.getAttendanceDate());
        return mapToResponse(saved);
    }

    @Transactional
    public List<AttendanceResponse> markBulkAttendance(BulkAttendanceRequest request, UUID markedById) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));
        User markedBy = userRepository.findById(markedById)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", markedById));

        List<AttendanceResponse> responses = new ArrayList<>();

        for (BulkAttendanceRequest.StudentAttendance record : request.getRecords()) {
            if (attendanceRepository.existsByStudentIdAndCourseIdAndAttendanceDate(
                    record.getStudentId(), request.getCourseId(), request.getAttendanceDate())) {
                log.warn("Skipping duplicate attendance for student {}", record.getStudentId());
                continue;
            }

            User student = userRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Student", "id", record.getStudentId()));

            Attendance attendance = Attendance.builder()
                    .student(student)
                    .course(course)
                    .attendanceDate(request.getAttendanceDate())
                    .status(record.getStatus())
                    .remarks(record.getRemarks())
                    .markedBy(markedBy)
                    .build();

            responses.add(mapToResponse(attendanceRepository.save(attendance)));
        }

        log.info("Bulk marked {} attendance records for course {} on {}",
                responses.size(), course.getCode(), request.getAttendanceDate());
        return responses;
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getAttendanceByCourseAndDate(UUID courseId, LocalDate date) {
        return attendanceRepository.findAllByCourseIdAndAttendanceDate(courseId, date).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AttendanceResponse> getStudentAttendance(UUID studentId, UUID courseId) {
        return attendanceRepository.findAllByStudentIdAndCourseId(studentId, courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AttendanceSummaryResponse getAttendanceSummary(UUID studentId, UUID courseId) {
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", studentId));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", courseId));

        long total = attendanceRepository.countTotalByStudentIdAndCourseId(studentId, courseId);
        long present = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.PRESENT);
        long absent = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.ABSENT);
        long late = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.LATE);
        long excused = attendanceRepository.countByStudentIdAndCourseIdAndStatus(studentId, courseId, AttendanceStatus.EXCUSED);

        double percentage = total > 0 ? ((double) (present + late) / total) * 100.0 : 0.0;

        return AttendanceSummaryResponse.builder()
                .studentId(studentId)
                .studentName(student.getFirstName() + " " + student.getLastName())
                .courseId(courseId)
                .courseName(course.getName())
                .totalClasses(total)
                .presentCount(present)
                .absentCount(absent)
                .lateCount(late)
                .excusedCount(excused)
                .attendancePercentage(Math.round(percentage * 100.0) / 100.0)
                .build();
    }

    private AttendanceResponse mapToResponse(Attendance attendance) {
        return AttendanceResponse.builder()
                .id(attendance.getId())
                .studentId(attendance.getStudent().getId())
                .studentName(attendance.getStudent().getFirstName() + " " + attendance.getStudent().getLastName())
                .courseId(attendance.getCourse().getId())
                .courseName(attendance.getCourse().getName())
                .attendanceDate(attendance.getAttendanceDate())
                .status(attendance.getStatus())
                .remarks(attendance.getRemarks())
                .markedByName(attendance.getMarkedBy() != null ?
                        attendance.getMarkedBy().getFirstName() + " " + attendance.getMarkedBy().getLastName() : null)
                .build();
    }
}
