package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InsightsService {

    InsightsService() { } // package-private constructor for testing

    /**
     * Aggregates all data into a single summary for the demo.
     */
    public static CampusInsights compute(
            List<Records.Student> students,
            List<Records.Attendance> attendance,
            List<Records.Room> rooms,
            List<Records.RoomBooking> bookings,
            List<Records.TimetableEntry> timetable,
            List<Records.Notice> notices) {

        // Compute average attendance
        double totalPresent = 0.0;
        for (Records.Attendance a : attendance) {
            if (a.status() == Records.AttendanceStatus.PRESENT ||
                a.status() == Records.AttendanceStatus.LATE) {
                totalPresent++;
            }
        }
        double averageAttendance = attendance.isEmpty() ? 0.0
                : 100.0 * totalPresent / attendance.size();

        // Count high-risk students
        int highRiskStudents = 0;
        for (Records.Student student : students) {
            int total = 0;
            int present = 0;
            for (Records.Attendance a : attendance) {
                if (a.studentId().equals(student.id())) {
                    total++;
                    if (a.status() == Records.AttendanceStatus.PRESENT ||
                        a.status() == Records.AttendanceStatus.LATE) {
                        present++;
                    }
                }
            }
            if (total > 0 && (100.0 * present / total) < 75.0) {
                highRiskStudents++;
            }
        }

        // Compute room utilisation
        Set<String> usedRooms = new HashSet<>();
        for (Records.RoomBooking b : bookings) {
            usedRooms.add(b.roomId());
        }
        double roomUtilisationPct = rooms.isEmpty() ? 0.0 : 100.0 * usedRooms.size() / rooms.size();

        // Group notices by category
        Map<String, Long> noticesByCategory = new LinkedHashMap<>();
        for (Records.Notice n : notices) {
            if (n.published() && (!n.expiresAt().isAfter(java.time.LocalDateTime.now()) || n.expiresAt() == null)) {
                noticesByCategory.merge(n.category().name(), 1L, Long::sum);
            }
        }

        // Find top rooms by usage
        Map<String, Long> roomUsage = new LinkedHashMap<>();
        for (Records.TimetableEntry t : timetable) {
            roomUsage.merge(t.roomId(), 1L, Long::sum);
        }
        List<String> topRooms = roomUsage.entrySet().stream()
                .sorted(Comparator.<Map.Entry<String, Long>>comparingLong(Map.Entry::getValue).reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        return new CampusInsights(
                averageAttendance,
                highRiskStudents,
                roomUtilisationPct,
                noticesByCategory,
                topRooms
        );
    }

    public record CampusInsights(
            double averageAttendance,
            int highRiskStudents,
            double roomUtilisationPct,
            Map<String, Long> noticesByCategory,
            List<String> topRooms) {}
}