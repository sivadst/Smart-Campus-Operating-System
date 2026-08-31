package com.campus.smartcampus.service;

import com.campus.smartcampus.entity.Attendance;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.repository.AttendanceRepository;
import com.campus.smartcampus.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExportService {

    private final AttendanceRepository attendanceRepository;
    private final CourseRepository courseRepository;

    @Transactional(readOnly = true)
    public byte[] exportCourseAttendanceCsv(UUID courseId, LocalDate date) {
        List<Attendance> records = attendanceRepository.findAllByCourseIdAndAttendanceDate(courseId, date);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("Student ID,Student Name,Email,Course Code,Course Name,Date,Status,Remarks,Marked By");

        for (Attendance a : records) {
            writer.printf("%s,\"%s %s\",%s,%s,\"%s\",%s,%s,\"%s\",\"%s\"%n",
                    a.getStudent().getId(),
                    a.getStudent().getFirstName(),
                    a.getStudent().getLastName(),
                    a.getStudent().getEmail(),
                    a.getCourse().getCode(),
                    a.getCourse().getName(),
                    a.getAttendanceDate(),
                    a.getStatus(),
                    a.getRemarks() != null ? a.getRemarks() : "",
                    a.getMarkedBy() != null ? a.getMarkedBy().getEmail() : "");
        }

        writer.flush();
        return out.toByteArray();
    }

    @Transactional(readOnly = true)
    public byte[] exportCourseCatalogCsv() {
        List<Course> courses = courseRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);

        writer.println("Course Code,Course Name,Department,Credits,Semester,Status,Max Enrollment,Current Enrollment");

        for (Course c : courses) {
            writer.printf("%s,\"%s\",\"%s\",%d,%d,%s,%d,%d%n",
                    c.getCode(),
                    c.getName(),
                    c.getDepartment() != null ? c.getDepartment().getName() : "N/A",
                    c.getCredits(),
                    c.getSemester(),
                    c.getStatus(),
                    c.getMaxEnrollment(),
                    c.getCurrentEnrollment());
        }

        writer.flush();
        return out.toByteArray();
    }
}
