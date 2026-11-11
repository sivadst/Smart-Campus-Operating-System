package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class NoticePrioritizerTest {

    @Test
    void testRank_returnsExpectedTop3() {
        // Setup: now=2026-10-05T09:30, audience "STUDENT"
        LocalDateTime now = LocalDateTime.of(2026, 10, 5, 9, 30);
        String audienceRole = "STUDENT";

        // Notices from DataSeeder
        List<Records.Notice> notices = List.of(
                new Records.Notice("N1", "Campus closed tomorrow - cyclone warning",
                        "All classes cancelled due to severe weather alert.",
                        Records.NoticeCategory.EMERGENCY, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 10, 4, 8, 0),
                        LocalDateTime.of(2026, 10, 20, 23, 59), "ALL"),
                new Records.Notice("N2", "End semester examination schedule released",
                        "Timetable available on the student portal from Monday.",
                        Records.NoticeCategory.EXAMINATION, "admin@smartcampus.edu",
                        true, true, LocalDateTime.of(2026, 9, 28, 12, 0),
                        LocalDateTime.of(2026, 11, 30, 23, 59), "ALL"),
                new Records.Notice("N3", "Revised syllabus for CS301 Database Systems",
                        "Unit 4 replaced with indexing and query optimisation.",
                        Records.NoticeCategory.ACADEMIC, "faculty@smartcampus.edu",
                        true, true, LocalDateTime.of(2026, 10, 1, 10, 0),
                        LocalDateTime.of(2026, 12, 31, 23, 59), "STUDENT"),
                new Records.Notice("N4", "Fee payment deadline extended to Oct 15",
                        "Pay semester fees through the portal before the new deadline.",
                        Records.NoticeCategory.ADMINISTRATIVE, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 25, 9, 30),
                        LocalDateTime.of(2026, 10, 15, 23, 59), "STUDENT"),
                new Records.Notice("N5", "Annual tech fest registrations now open",
                        "Register your team before Oct 10 for Hackathon 2026.",
                        Records.NoticeCategory.EVENT, "student@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 20, 15, 0),
                        LocalDateTime.of(2026, 10, 10, 23, 59), "ALL"),
                new Records.Notice("N6", "TCS campus drive this Friday",
                        "Eligible final-year students must register on the placement cell.",
                        Records.NoticeCategory.PLACEMENT, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 10, 3, 14, 0),
                        LocalDateTime.of(2026, 10, 12, 23, 59), "STUDENT"),
                new Records.Notice("N7", "Inter-college cricket trials",
                        "Selection trials on Saturday morning at the main ground.",
                        Records.NoticeCategory.SPORTS, "faculty@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 18, 11, 0),
                        LocalDateTime.of(2026, 10, 8, 23, 59), "ALL"),
                new Records.Notice("N8", "Cultural night auditions",
                        "Auditions for dance and music on Wednesday after class hours.",
                        Records.NoticeCategory.CULTURAL, "student@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 22, 16, 0),
                        LocalDateTime.of(2026, 10, 9, 23, 59), "ALL"),
                new Records.Notice("N9", "Library timings changed during exams",
                        "Library will remain open until 22:00 from next week.",
                        Records.NoticeCategory.GENERAL, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 15, 9, 0),
                        LocalDateTime.of(2026, 11, 10, 23, 59), "ALL"),
                new Records.Notice("N10", "Old bus route notice (EXPIRED)",
                        "This notice expired and must be dropped from the feed.",
                        Records.NoticeCategory.GENERAL, "transport@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 8, 1, 9, 0),
                        LocalDateTime.of(2026, 8, 15, 23, 59), "ALL")
        );

        // Test: Top 3: N2 (score 10.0), N1 (score 9.0), N3 (score 9.0)
        // N10 (expired 2026-08-15) must be absent from results
        List<NoticePrioritizer.ScoredNotice> result = NoticePrioritizer.rank(notices, now, audienceRole, 3);

        assertEquals(3, result.size(), "Expected 3 results but got " + result.size());

        // Check 1st: N2 (score 10.0)
        NoticePrioritizer.ScoredNotice first = result.get(0);
        assertEquals("N2", first.notice().id(), "1st place expected N2 but got " + first.notice().id());
        assertEquals(10.0, first.score(), 0.001, "N2 expected score 10.0 but got " + first.score());

        // Check 2nd: N1 (score 9.0)
        NoticePrioritizer.ScoredNotice second = result.get(1);
        assertEquals("N1", second.notice().id(), "2nd place expected N1 but got " + second.notice().id());
        assertEquals(9.0, second.score(), 0.001, "N1 expected score 9.0 but got " + second.score());

        // Check 3rd: N3 (score 9.0)
        NoticePrioritizer.ScoredNotice third = result.get(2);
        assertEquals("N3", third.notice().id(), "3rd place expected N3 but got " + third.notice().id());
        assertEquals(9.0, third.score(), 0.001, "N3 expected score 9.0 but got " + third.score());

        // Verify N10 is NOT in results (expired)
        for (NoticePrioritizer.ScoredNotice sn : result) {
            assertFalse("N10".equals(sn.notice().id()), "N10 (expired) should not be in results");
        }

        // Also verify no expired notices in full list
        List<NoticePrioritizer.ScoredNotice> allResults = NoticePrioritizer.rank(notices, now, audienceRole, 10);
        for (NoticePrioritizer.ScoredNotice sn : allResults) {
            assertFalse("N10".equals(sn.notice().id()), "N10 (expired) should not be in full results");
        }
    }
}