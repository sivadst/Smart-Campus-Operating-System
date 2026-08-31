package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.CourseRequest;
import com.campus.smartcampus.dto.response.CourseResponse;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.Department;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.CourseStatus;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.DepartmentRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Course", "code", request.getCode());
        }

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department", "id", request.getDepartmentId()));

        Course.CourseBuilder builder = Course.builder()
                .code(request.getCode())
                .name(request.getName())
                .description(request.getDescription())
                .credits(request.getCredits())
                .semester(request.getSemester())
                .department(department)
                .maxEnrollment(request.getMaxEnrollment() > 0 ? request.getMaxEnrollment() : 60);

        if (request.getFacultyId() != null) {
            User faculty = userRepository.findById(request.getFacultyId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getFacultyId()));
            builder.faculty(faculty);
        }

        Course saved = courseRepository.save(builder.build());
        log.info("Created course: {} ({})", saved.getName(), saved.getCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public CourseResponse getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));
        return mapToResponse(course);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesByDepartment(UUID departmentId) {
        return courseRepository.findAllByDepartmentId(departmentId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CourseResponse updateCourse(UUID id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", id));

        course.setName(request.getName());
        course.setDescription(request.getDescription());
        course.setCredits(request.getCredits());
        course.setSemester(request.getSemester());
        course.setMaxEnrollment(request.getMaxEnrollment());

        Course saved = courseRepository.save(course);
        log.info("Updated course: {}", saved.getCode());
        return mapToResponse(saved);
    }

    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .code(course.getCode())
                .name(course.getName())
                .description(course.getDescription())
                .credits(course.getCredits())
                .semester(course.getSemester())
                .departmentId(course.getDepartment().getId())
                .departmentName(course.getDepartment().getName())
                .facultyId(course.getFaculty() != null ? course.getFaculty().getId() : null)
                .facultyName(course.getFaculty() != null ? course.getFaculty().getFirstName() + " " + course.getFaculty().getLastName() : null)
                .status(course.getStatus())
                .maxEnrollment(course.getMaxEnrollment())
                .currentEnrollment(course.getCurrentEnrollment())
                .build();
    }
}
