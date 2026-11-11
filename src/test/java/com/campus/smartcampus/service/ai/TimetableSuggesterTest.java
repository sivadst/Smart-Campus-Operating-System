package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import org.junit.jupiter.api.Test;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class TimetableSuggesterTest {

    @Test
    void testSuggest_returnsExpectedMondaySlotsForFacultyF1() {
        // Setup: course capacity 60, faculty "F1", 4 existing entries (T1, T2, T3, T5)
        Records.Course course = new Records.Course("C1", "CS101", "Programming Fundamentals", 4, "D1", 60);
        String facultyId = "F1";

        // Use the rooms from DataSeeder (need at least R1 and R6 which have capacity >= 60)
        List<Records.Room> rooms = List.of(
                new Records.Room("R1", "LH-301", "Lecture Hall 301", Records.RoomType.LECTURE_HALL, 120, 3, true, true, true),
                new Records.Room("R6", "CL-01", "Computer Lab 01", Records.RoomType.COMPUTER_LAB, 60, 2, true, true, true)
        );

        // Existing timetable entries - T1, T2, T3, T5 from DataSeeder
        List<Records.TimetableEntry> existing = List.of(
                new Records.TimetableEntry("T1", "C1", "R1", "F1", DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)),
                new Records.TimetableEntry("T2", "C2", "R6", "F1", DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)),
                new Records.TimetableEntry("T3", "C1", "R1", "F2", DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)),
                new Records.TimetableEntry("T5", "C2", "R6", "F2", DayOfWeek.TUESDAY, LocalTime.of(12, 0), LocalTime.of(13, 0))
        );

        // Test: First 3 valid MONDAY slots for F1 should be:
        // 08:00-09:00, 14:00-15:00, 15:00-16:00
        // 09:00-10:00 and 10:00-11:00 are excluded due to faculty clash
        List<TimetableSuggester.SuggestedSlot> result = TimetableSuggester.suggest(course, facultyId, rooms, existing, 5);

        // Check we got at least 3 results
        assertTrue(result.size() >= 3, "Expected at least 3 slots but got " + result.size());

        // Check first 3 are the expected Monday slots
        TimetableSuggester.SuggestedSlot slot0 = result.get(0);
        TimetableSuggester.SuggestedSlot slot1 = result.get(1);
        TimetableSuggester.SuggestedSlot slot2 = result.get(2);

        // First slot: MONDAY 08:00-09:00
        assertEquals(DayOfWeek.MONDAY, slot0.day(), "First slot expected MONDAY");
        assertEquals(LocalTime.of(8, 0), slot0.start(), "First slot start");
        assertEquals(LocalTime.of(9, 0), slot0.end(), "First slot end");

        // Second slot: MONDAY 14:00-15:00
        assertEquals(DayOfWeek.MONDAY, slot1.day(), "Second slot expected MONDAY");
        assertEquals(LocalTime.of(14, 0), slot1.start(), "Second slot start");
        assertEquals(LocalTime.of(15, 0), slot1.end(), "Second slot end");

        // Third slot: MONDAY 15:00-16:00
        assertEquals(DayOfWeek.MONDAY, slot2.day(), "Third slot expected MONDAY");
        assertEquals(LocalTime.of(15, 0), slot2.start(), "Third slot start");
        assertEquals(LocalTime.of(16, 0), slot2.end(), "Third slot end");

        // Also verify 09:00-10:00 and 10:00-11:00 are NOT in the results (faculty clash)
        for (TimetableSuggester.SuggestedSlot slot : result) {
            if (slot.day() == DayOfWeek.MONDAY) {
                assertFalse(slot.start().equals(LocalTime.of(9, 0)) && slot.end().equals(LocalTime.of(10, 0)),
                    "09:00-10:00 should be excluded due to faculty clash (T1)");
                assertFalse(slot.start().equals(LocalTime.of(10, 0)) && slot.end().equals(LocalTime.of(11, 0)),
                    "10:00-11:00 should be excluded due to faculty clash (T2)");
            }
        }
    }
}