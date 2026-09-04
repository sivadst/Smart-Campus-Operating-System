package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.TimetableEntry;
import com.campus.smartcampus.enums.DayOfWeekEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TimetableEntryRepository extends JpaRepository<TimetableEntry, UUID> {
    List<TimetableEntry> findAllByFacultyIdAndDayOfWeek(UUID facultyId, DayOfWeekEnum day);
    List<TimetableEntry> findAllByCourseId(UUID courseId);
    List<TimetableEntry> findAllByRoomIdAndDayOfWeek(UUID roomId, DayOfWeekEnum day);
    List<TimetableEntry> findAllByFacultyIdAndAcademicYearAndSemester(UUID facultyId, String academicYear, int semester);
}
