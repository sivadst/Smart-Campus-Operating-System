package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.EnrollmentRequest;
import com.campus.smartcampus.dto.response.EnrollmentResponse;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.Enrollment;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.EnrollmentRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public EnrollmentResponse enrollStudent(EnrollmentRequest request) {
        if (enrollmentRepository.existsByStudentIdAndCourseIdAndIsActiveTrue(
                request.getStudentId(), request.getCourseId())) {
            throw new DuplicateResourceException("Enrollment", "student+course",
                    request.getStudentId() + "/" + request.getCourseId());
        }

        User student = userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", request.getStudentId()));
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));

        if (course.getCurrentEnrollment() >= course.getMaxEnrollment()) {
            throw new BadRequestException("Course " + course.getCode() + " has reached maximum enrollment capacity");
        }

        Enrollment enrollment = Enrollment.builder()
                .student(student)
                .course(course)
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .build();

        Enrollment saved = enrollmentRepository.save(enrollment);

        course.setCurrentEnrollment(course.getCurrentEnrollment() + 1);
        courseRepository.save(course);

        log.info("Enrolled student {} in course {}", student.getEmail(), course.getCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getStudentEnrollments(UUID studentId) {
        return enrollmentRepository.findAllByStudentIdAndIsActiveTrue(studentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EnrollmentResponse> getCourseEnrollments(UUID courseId) {
        return enrollmentRepository.findAllByCourseIdAndIsActiveTrue(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public EnrollmentResponse updateGrade(UUID enrollmentId, String grade) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        enrollment.setGrade(grade);
        Enrollment saved = enrollmentRepository.save(enrollment);
        log.info("Updated grade for enrollment {}: {}", enrollmentId, grade);
        return mapToResponse(saved);
    }

    @Transactional
    public void withdrawEnrollment(UUID enrollmentId) {
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Enrollment", "id", enrollmentId));

        enrollment.setActive(false);
        enrollmentRepository.save(enrollment);

        Course course = enrollment.getCourse();
        course.setCurrentEnrollment(Math.max(0, course.getCurrentEnrollment() - 1));
        courseRepository.save(course);

        log.info("Withdrew enrollment {} for student {} from course {}",
                enrollmentId, enrollment.getStudent().getEmail(), course.getCode());
    }

    private EnrollmentResponse mapToResponse(Enrollment enrollment) {
        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getFirstName() + " " + enrollment.getStudent().getLastName())
                .studentEmail(enrollment.getStudent().getEmail())
                .courseId(enrollment.getCourse().getId())
                .courseCode(enrollment.getCourse().getCode())
                .courseName(enrollment.getCourse().getName())
                .enrolledAt(enrollment.getEnrolledAt())
                .isActive(enrollment.isActive())
                .grade(enrollment.getGrade())
                .academicYear(enrollment.getAcademicYear())
                .semester(enrollment.getSemester())
                .build();
    }
}
