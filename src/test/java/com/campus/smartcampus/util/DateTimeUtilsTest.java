package com.campus.smartcampus.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DateTimeUtils Unit Tests")
class DateTimeUtilsTest {

    @Test
    @DisplayName("formatDate should return yyyy-MM-dd string for valid date")
    void formatDate_ValidDate_ReturnsFormatted() {
        LocalDate date = LocalDate.of(2026, 10, 15);
        assertEquals("2026-10-15", DateTimeUtils.formatDate(date));
    }

    @Test
    @DisplayName("formatDate should return null for null input")
    void formatDate_NullInput_ReturnsNull() {
        assertNull(DateTimeUtils.formatDate(null));
    }

    @Test
    @DisplayName("formatDateTime should return formatted IST datetime for valid instant")
    void formatDateTime_ValidInstant_ReturnsFormatted() {
        // 2026-10-15T10:30:00Z = 2026-10-15 16:00:00 IST (UTC+5:30)
        Instant instant = Instant.parse("2026-10-15T10:30:00Z");
        String result = DateTimeUtils.formatDateTime(instant);

        assertNotNull(result);
        assertTrue(result.startsWith("2026-10-15"), "Should contain the date portion");
        assertTrue(result.contains(":"), "Should contain time separator");
    }

    @Test
    @DisplayName("formatDateTime should return null for null input")
    void formatDateTime_NullInput_ReturnsNull() {
        assertNull(DateTimeUtils.formatDateTime(null));
    }

    @Test
    @DisplayName("parseDate should parse yyyy-MM-dd string to LocalDate")
    void parseDate_ValidString_ReturnsLocalDate() {
        LocalDate result = DateTimeUtils.parseDate("2026-10-15");
        assertEquals(LocalDate.of(2026, 10, 15), result);
    }

    @Test
    @DisplayName("parseDate should return null for null input")
    void parseDate_NullInput_ReturnsNull() {
        assertNull(DateTimeUtils.parseDate(null));
    }

    @Test
    @DisplayName("isWeekday should return true for Monday through Friday")
    void isWeekday_Weekdays_ReturnsTrue() {
        // 2026-10-12 is Monday, 2026-10-16 is Friday
        assertTrue(DateTimeUtils.isWeekday(LocalDate.of(2026, 10, 12))); // Monday
        assertTrue(DateTimeUtils.isWeekday(LocalDate.of(2026, 10, 14))); // Wednesday
        assertTrue(DateTimeUtils.isWeekday(LocalDate.of(2026, 10, 16))); // Friday
    }

    @Test
    @DisplayName("isWeekday should return false for Saturday and Sunday")
    void isWeekday_Weekends_ReturnsFalse() {
        // 2026-10-17 is Saturday, 2026-10-18 is Sunday
        assertFalse(DateTimeUtils.isWeekday(LocalDate.of(2026, 10, 17))); // Saturday
        assertFalse(DateTimeUtils.isWeekday(LocalDate.of(2026, 10, 18))); // Sunday
    }

    @Test
    @DisplayName("getCurrentAcademicYear should return year range in yyyy-yyyy format")
    void getCurrentAcademicYear_ReturnsValidFormat() {
        String academicYear = DateTimeUtils.getCurrentAcademicYear();

        assertNotNull(academicYear);
        assertTrue(academicYear.matches("\\d{4}-\\d{4}"), "Should match yyyy-yyyy pattern");

        String[] parts = academicYear.split("-");
        int startYear = Integer.parseInt(parts[0]);
        int endYear = Integer.parseInt(parts[1]);
        assertEquals(1, endYear - startYear, "End year should be exactly 1 more than start year");
    }

    @Test
    @DisplayName("getCurrentSemester should return 1 or 2")
    void getCurrentSemester_ReturnsValidSemester() {
        int semester = DateTimeUtils.getCurrentSemester();
        assertTrue(semester == 1 || semester == 2, "Semester must be 1 or 2");
    }

    @Test
    @DisplayName("Constructor should throw UnsupportedOperationException")
    void constructor_ThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // Use reflection to invoke private constructor
            var constructor = DateTimeUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw (UnsupportedOperationException) e.getCause();
            }
        });
    }
}
