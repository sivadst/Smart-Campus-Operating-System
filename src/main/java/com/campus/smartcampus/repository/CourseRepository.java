package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.enums.CourseStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CourseRepository extends JpaRepository<Course, UUID> {
    Optional<Course> findByCode(String code);
    boolean existsByCode(String code);
    List<Course> findAllByDepartmentId(UUID departmentId);
    Page<Course> findAllByStatus(CourseStatus status, Pageable pageable);
    List<Course> findAllByFacultyId(UUID facultyId);

    @Query("SELECT c FROM Course c WHERE c.department.id = :deptId AND c.semester = :semester AND c.status = 'ACTIVE'")
    List<Course> findActiveCoursesByDepartmentAndSemester(
            @Param("deptId") UUID departmentId,
            @Param("semester") int semester);
}
