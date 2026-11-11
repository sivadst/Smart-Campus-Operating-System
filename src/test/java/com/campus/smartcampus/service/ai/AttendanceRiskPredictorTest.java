package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AttendanceRiskPredictorTest {

    @Test
    void testRisk_PPPA_returnsMedium() {
        List<Records.Attendance> history = createHistory("S1", "PPPA");
        String result = AttendanceRiskPredictor.risk(history);
        assertEquals("MEDIUM", result, "[P,P,P,A] expected MEDIUM (75% boundary)");
    }

    @Test
    void testRisk_PPPP_returnsLow() {
        List<Records.Attendance> history = createHistory("S2", "PPPP");
        String result = AttendanceRiskPredictor.risk(history);
        assertEquals("LOW", result, "[P,P,P,P] expected LOW");
    }

    @Test
    void testRisk_AAAP_returnsHigh() {
        List<Records.Attendance> history = createHistory("S3", "AAAP");
        String result = AttendanceRiskPredictor.risk(history);
        assertEquals("HIGH", result, "[A,A,A,P] expected HIGH");
    }

    @Test
    void testRisk_empty_returnsHigh() {
        List<Records.Attendance> history = new ArrayList<>();
        String result = AttendanceRiskPredictor.risk(history);
        assertEquals("HIGH", result, "[] (empty) expected HIGH");
    }

    @Test
    void testRisk_PAPP_returnsMedium() {
        List<Records.Attendance> history = createHistory("S16", "PAPP");
        String result = AttendanceRiskPredictor.risk(history);
        assertEquals("MEDIUM", result, "[P,A,P,P] expected MEDIUM (75% boundary)");
    }

    private static List<Records.Attendance> createHistory(String studentId, String pattern) {
        List<Records.Attendance> history = new ArrayList<>();
        for (int i = 0; i < pattern.length(); i++) {
            Records.AttendanceStatus status = switch (pattern.charAt(i)) {
                case 'P' -> Records.AttendanceStatus.PRESENT;
                case 'A' -> Records.AttendanceStatus.ABSENT;
                case 'L' -> Records.AttendanceStatus.LATE;
                case 'E' -> Records.AttendanceStatus.EXCUSED;
                default -> Records.AttendanceStatus.PRESENT;
            };
            LocalDate date = LocalDate.of(2026, 9, 1).plusDays(i);
            history.add(new Records.Attendance("A" + i, studentId, "C1", date, status));
        }
        return history;
    }
}