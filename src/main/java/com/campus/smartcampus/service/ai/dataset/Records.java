package com.campus.smartcampus.service.ai.dataset;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Shared domain records for the Smart Campus "AI" track.
 *
 * <p>These mirror the entity shapes described in the Smart Campus OS
 * repository so the UI and backend tracks can swap in their own DTOs later
 * without changing the algorithm signatures.
 */
public final class Records {

    private Records() {
    }

    /** Attendance states, matching the README's attendance module. */
    public enum AttendanceStatus {
        PRESENT, ABSENT, LATE, EXCUSED, ON_LEAVE
    }

    /** Room categories, matching the README's 11 room types. */
    public enum RoomType {
        LECTURE_HALL, LABORATORY, SEMINAR_ROOM, LIBRARY, AUDITORIUM,
        COMPUTER_LAB, CONFERENCE_ROOM, OFFICE, CAFETERIA, GYMNASIUM, WORKSHOP
    }

    /** Notice categories, matching the README's 9 notice categories. */
    public enum NoticeCategory {
        ACADEMIC, ADMINISTRATIVE, EVENT, EXAMINATION,
        PLACEMENT, SPORTS, CULTURAL, EMERGENCY, GENERAL
    }

    public record Student(String id, String name, String email, String departmentId) {
    }

    public record Department(String id, String code, String name) {
    }

    public record Course(String id, String code, String name, int credits,
                         String departmentId, int capacity) {
    }

    /** One day's attendance for one student in one course. */
    public record Attendance(String id, String studentId, String courseId,
                             LocalDate date, AttendanceStatus status) {
    }

    public record Room(String id, String code, String name, RoomType type,
                       int capacity, int floor,
                       boolean hasProjector, boolean hasAc, boolean hasWifi) {
    }

    public record RoomBooking(String id, String roomId, String bookedBy, String purpose,
                              LocalDateTime start, LocalDateTime end, String status) {
    }

    public record TimetableEntry(String id, String courseId, String roomId, String facultyId,
                                 DayOfWeek day, LocalTime start, LocalTime end) {
    }

    public record Notice(String id, String title, String content, NoticeCategory category,
                         String authorId, boolean pinned, boolean published,
                         LocalDateTime publishedAt, LocalDateTime expiresAt,
                         String targetRole) {
    }

    /** One authentication attempt, used by the security analyser. */
    public record LoginEvent(String id, String email, String ip,
                             LocalDateTime timestamp, boolean success) {
    }
}
