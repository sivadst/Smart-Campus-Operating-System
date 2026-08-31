package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Attendance;
import com.campus.smartcampus.enums.AttendanceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    List<Attendance> findAllByStudentIdAndCourseId(UUID studentId, UUID courseId);
    List<Attendance> findAllByCourseIdAndAttendanceDate(UUID courseId, LocalDate date);
    boolean existsByStudentIdAndCourseIdAndAttendanceDate(UUID studentId, UUID courseId, LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.course.id = :courseId AND a.status = :status")
    long countByStudentIdAndCourseIdAndStatus(
            @Param("studentId") UUID studentId,
            @Param("courseId") UUID courseId,
            @Param("status") AttendanceStatus status);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.student.id = :studentId AND a.course.id = :courseId")
    long countTotalByStudentIdAndCourseId(
            @Param("studentId") UUID studentId,
            @Param("courseId") UUID courseId);
}
