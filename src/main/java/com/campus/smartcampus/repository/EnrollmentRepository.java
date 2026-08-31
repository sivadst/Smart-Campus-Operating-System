package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    List<Enrollment> findAllByStudentIdAndIsActiveTrue(UUID studentId);
    List<Enrollment> findAllByCourseIdAndIsActiveTrue(UUID courseId);
    Optional<Enrollment> findByStudentIdAndCourseId(UUID studentId, UUID courseId);
    boolean existsByStudentIdAndCourseIdAndIsActiveTrue(UUID studentId, UUID courseId);
    long countByCourseIdAndIsActiveTrue(UUID courseId);
}
