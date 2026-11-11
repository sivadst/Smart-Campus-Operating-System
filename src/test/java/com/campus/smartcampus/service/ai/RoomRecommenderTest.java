package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class RoomRecommenderTest {

    @Test
    void testRecommend_returnsExpectedRanking() {
        // Setup: course capacity 100, needsProjector=true, slot 2026-10-05T09:00-10:00
        Records.Course course = new Records.Course("C3", "CS301", "Database Systems", 3, "D1", 100);
        boolean needsProjector = true;
        LocalDateTime slotStart = LocalDateTime.of(2026, 10, 5, 9, 0);
        LocalDateTime slotEnd = LocalDateTime.of(2026, 10, 5, 10, 0);

        // Rooms from DataSeeder
        List<Records.Room> rooms = List.of(
                new Records.Room("R1", "LH-301", "Lecture Hall 301", Records.RoomType.LECTURE_HALL, 120, 3, true, true, true),
                new Records.Room("R2", "LH-305", "Lecture Hall 305", Records.RoomType.LECTURE_HALL, 100, 3, false, false, true),
                new Records.Room("R3", "AUD-01", "Main Auditorium", Records.RoomType.AUDITORIUM, 500, 1, true, true, true),
                new Records.Room("R4", "LAB-102", "Programming Lab 102", Records.RoomType.LABORATORY, 40, 1, true, true, true),
                new Records.Room("R8", "LH-401", "Lecture Hall 401", Records.RoomType.LECTURE_HALL, 80, 4, true, false, true)
        );

        // Bookings: LH-301 (R1) is booked at the slot, R2 is free
        List<Records.RoomBooking> bookings = List.of(
                new Records.RoomBooking("B1", "R1", "faculty@smartcampus.edu",
                        "CS201 class", slotStart, slotEnd, "CONFIRMED"),
                new Records.RoomBooking("B2", "R2", "library@smartcampus.edu",
                        "Book review", LocalDateTime.of(2026, 10, 5, 14, 0),
                        LocalDateTime.of(2026, 10, 5, 15, 0), "CONFIRMED"),
                new Records.RoomBooking("B3", "R6", "student@smartcampus.edu",
                        "Project meeting", LocalDateTime.of(2026, 10, 5, 11, 0),
                        LocalDateTime.of(2026, 10, 5, 12, 0), "PENDING")
        );

        // Test: Expected ranking
        // Winner: R2 (LH-305) with score 16.0
        // 2nd: R1 (LH-301) with score 13.0 (booked at slot)
        // 3rd: R3 (AUD-01) with score 10.0 (too large)
        // R4 (40 seats) and R8 (80 seats) disqualified (< 100 capacity)
        List<RoomRecommender.RoomScore> result = RoomRecommender.recommend(course, needsProjector, rooms, bookings, slotStart, slotEnd, 5);

        assertTrue(result.size() >= 3, "Expected at least 3 results but got " + result.size());

        RoomRecommender.RoomScore first = result.get(0);
        RoomRecommender.RoomScore second = result.get(1);
        RoomRecommender.RoomScore third = result.get(2);

        // Check winner: R2 (LH-305) with score 16.0
        assertEquals("R2", first.room().id(), "Winner expected R2 (LH-305) but got " + first.room().id() + " (" + first.room().code() + ")");
        assertEquals(16.0, first.score(), 0.001, "Winner R2 expected score 16.0 but got " + first.score());

        // Check 2nd: R1 (LH-301) with score 13.0 (booked at slot)
        assertEquals("R1", second.room().id(), "2nd place expected R1 (LH-301) but got " + second.room().id() + " (" + second.room().code() + ")");
        assertEquals(13.0, second.score(), 0.001, "2nd place R1 expected score 13.0 but got " + second.score());

        // Check 3rd: R3 (AUD-01) with score 10.0 (too large)
        assertEquals("R3", third.room().id(), "3rd place expected R3 (AUD-01) but got " + third.room().id() + " (" + third.room().code() + ")");
        assertEquals(10.0, third.score(), 0.001, "3rd place R3 expected score 10.0 but got " + third.score());

        // Verify R4 and R8 are disqualified (not in results)
        for (RoomRecommender.RoomScore rs : result) {
            assertFalse("R4".equals(rs.room().id()) || "R8".equals(rs.room().id()),
                "Room " + rs.room().id() + " should be disqualified (capacity < 100)");
        }
    }
}