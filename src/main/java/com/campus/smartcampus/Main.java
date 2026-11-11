package com.campus.smartcampus;

import com.campus.smartcampus.service.ai.AttendanceRiskPredictor;
import com.campus.smartcampus.service.ai.NoticePrioritizer;
import com.campus.smartcampus.service.ai.RoomRecommender;
import com.campus.smartcampus.service.ai.SecurityAnalyser;
import com.campus.smartcampus.service.ai.TimetableSuggester;
import com.campus.smartcampus.service.ai.dataset.DataSeeder;
import com.campus.smartcampus.service.ai.dataset.Records;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        demoAttendanceRisk();
        demoTimetableSuggester();
        demoRoomRecommender();
        demoNoticePrioritizer();
        demoSecurityAnalyser();
        System.out.println("=== DEMO COMPLETE ===");
    }

    private static void demoAttendanceRisk() {
        System.out.println("=== ATTENDANCE RISK ===");
        List<Records.Attendance> history = DataSeeder.historyFor("S1");
        String risk = AttendanceRiskPredictor.risk(history);
        System.out.println("Result: " + risk + " risk for S1");
        System.out.println();
    }

    private static void demoTimetableSuggester() {
        System.out.println("=== TIMETABLE SUGGESTER ===");
        Records.Course course = DataSeeder.demoCourse();
        String facultyId = DataSeeder.DEMO_FACULTY_ID;
        List<Records.Room> rooms = List.of(
                DataSeeder.rooms().get(0), // R1
                DataSeeder.rooms().get(1), // R2
                DataSeeder.rooms().get(5)  // R6
        );
        List<Records.TimetableEntry> existing = DataSeeder.timetable();
        List<TimetableSuggester.SuggestedSlot> slots =
                TimetableSuggester.suggest(course, facultyId, rooms, existing, 3);
        System.out.println("Result: " + slots.size() + " valid slots found");
        System.out.println();
    }

    private static void demoRoomRecommender() {
        System.out.println("=== ROOM RECOMMENDER ===");
        Records.Course course = DataSeeder.demoCourse(); // C3 - capacity 100, needsProjector=true
        boolean needsProjector = true;
        List<Records.Room> rooms = DataSeeder.rooms();
        List<Records.RoomBooking> bookings = DataSeeder.bookings();
        LocalDateTime slotStart = DataSeeder.CS301_SLOT_START;
        LocalDateTime slotEnd = DataSeeder.CS301_SLOT_END;
        List<RoomRecommender.RoomScore> results =
                RoomRecommender.recommend(course, needsProjector, rooms, bookings, slotStart, slotEnd, 1);
        if (!results.isEmpty()) {
            RoomRecommender.RoomScore winner = results.get(0);
            System.out.println("Result: 1 winner (" + winner.room().code() + ") with score " + winner.score());
        } else {
            System.out.println("Result: 0 winners");
        }
        System.out.println();
    }

    private static void demoNoticePrioritizer() {
        System.out.println("=== NOTICE PRIORITIZER ===");
        List<Records.Notice> notices = DataSeeder.notices();
        LocalDateTime now = DataSeeder.DEMO_NOW;
        String audienceRole = "STUDENT";
        List<NoticePrioritizer.ScoredNotice> ranked =
                NoticePrioritizer.rank(notices, now, audienceRole, 3);
        StringBuilder ids = new StringBuilder();
        for (int i = 0; i < ranked.size(); i++) {
            if (i > 0) {
                ids.append(", ");
            }
            ids.append(ranked.get(i).notice().id());
        }
        System.out.println("Result: Top 3 notices: " + ids);
        System.out.println();
    }

    private static void demoSecurityAnalyser() {
        System.out.println("=== SECURITY ANALYSER ===");
        List<Records.LoginEvent> events = DataSeeder.loginEvents();
        List<SecurityAnalyser.Finding> findings = SecurityAnalyser.flag(events);
        long highCount = findings.stream().filter(f -> f.severity().equals("HIGH")).count();
        long mediumCount = findings.stream().filter(f -> f.severity().equals("MEDIUM")).count();
        long lowCount = findings.stream().filter(f -> f.severity().equals("LOW")).count();
        System.out.println("Result: " + findings.size() + " findings: " + highCount + " HIGH, " + mediumCount + " MEDIUM");
        System.out.println();
    }
}