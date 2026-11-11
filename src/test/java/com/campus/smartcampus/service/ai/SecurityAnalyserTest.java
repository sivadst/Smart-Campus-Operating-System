package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class SecurityAnalyserTest {

    @Test
    void testFlag_returnsExpectedFindings() {
        // Setup: the 10 events from DataSeeder.loginEvents()
        List<Records.LoginEvent> events = List.of(
                new Records.LoginEvent("L1", "bala@smartcampus.edu", "10.0.0.5",
                        LocalDateTime.of(2026, 10, 5, 8, 30), true),
                new Records.LoginEvent("L2", "anitha@smartcampus.edu", "10.0.0.9",
                        LocalDateTime.of(2026, 10, 5, 8, 45), true),
                new Records.LoginEvent("L3", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 10), false),
                new Records.LoginEvent("L4", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 12), false),
                new Records.LoginEvent("L5", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 14), false),
                new Records.LoginEvent("L6", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 15), false),
                new Records.LoginEvent("L7", "indira@smartcampus.edu", "10.0.0.21",
                        LocalDateTime.of(2026, 10, 5, 3, 10), true),
                new Records.LoginEvent("L8", "chitra@smartcampus.edu", "10.0.0.14",
                        LocalDateTime.of(2026, 10, 5, 9, 5), false),
                new Records.LoginEvent("L9", "deepak@smartcampus.edu", "10.0.0.33",
                        LocalDateTime.of(2026, 10, 5, 9, 15), true),
                new Records.LoginEvent("L10", "eswari@smartcampus.edu", "10.0.0.41",
                        LocalDateTime.of(2026, 10, 5, 9, 25), true)
        );

        // Test the findings
        List<SecurityAnalyser.Finding> findings = SecurityAnalyser.flag(events);

        // Check findings for brute@smartcampus.edu
        boolean bruteBruteForce = false;
        boolean bruteOffHours = false;
        boolean indiraOffHours = false;
        boolean chitraHasFindings = false;

        for (SecurityAnalyser.Finding f : findings) {
            if ("brute@smartcampus.edu".equals(f.subject())) {
                if ("BRUTE_FORCE_SUSPECTED".equals(f.type())) {
                    bruteBruteForce = true;
                    assertEquals("HIGH", f.severity(), "brute BRUTE_FORCE_SUSPECTED should be HIGH severity");
                }
                if ("OFF_HOURS_ACCESS".equals(f.type())) {
                    bruteOffHours = true;
                }
            }
            if ("indira@smartcampus.edu".equals(f.subject()) && "OFF_HOURS_ACCESS".equals(f.type())) {
                indiraOffHours = true;
                assertEquals("MEDIUM", f.severity(), "indira OFF_HOURS_ACCESS should be MEDIUM severity");
            }
            if ("chitra@smartcampus.edu".equals(f.subject())) {
                chitraHasFindings = true;
            }
        }

        // According to the actual code behavior:
        // - brute gets BRUTE_FORCE_SUSPECTED (4 failures in 5 min window)
        // - brute does NOT get OFF_HOURS_ACCESS (no successful logins)
        // - indira gets OFF_HOURS_ACCESS (successful login at 03:10)
        // - chitra gets nothing (only 1 failure, less than 5)
        // - brute does NOT get REPEATED_FAILURES (only 4 failures, threshold is 5)

        assertTrue(bruteBruteForce, "brute@smartcampus.edu should have BRUTE_FORCE_SUSPECTED finding");
        assertFalse(bruteOffHours, "brute@smartcampus.edu should NOT have OFF_HOURS_ACCESS (no successful logins)");
        assertTrue(indiraOffHours, "indira@smartcampus.edu should have OFF_HOURS_ACCESS finding");
        assertFalse(chitraHasFindings, "chitra@smartcampus.edu should have no findings (only 1 failure)");

        // Also verify ordering: HIGH > MEDIUM > LOW
        assertTrue(findings.size() >= 2, "Expected at least 2 findings");
        assertEquals("HIGH", findings.get(0).severity(), "First finding should be HIGH severity");
        assertEquals("MEDIUM", findings.get(1).severity(), "Second finding should be MEDIUM severity");
    }
}