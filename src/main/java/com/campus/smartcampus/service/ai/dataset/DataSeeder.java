package com.campus.smartcampus.service.ai.dataset;

import com.campus.smartcampus.service.ai.dataset.Records.Attendance;
import com.campus.smartcampus.service.ai.dataset.Records.AttendanceStatus;
import com.campus.smartcampus.service.ai.dataset.Records.Course;
import com.campus.smartcampus.service.ai.dataset.Records.Department;
import com.campus.smartcampus.service.ai.dataset.Records.LoginEvent;
import com.campus.smartcampus.service.ai.dataset.Records.Notice;
import com.campus.smartcampus.service.ai.dataset.Records.NoticeCategory;
import com.campus.smartcampus.service.ai.dataset.Records.Room;
import com.campus.smartcampus.service.ai.dataset.Records.RoomBooking;
import com.campus.smartcampus.service.ai.dataset.Records.RoomType;
import com.campus.smartcampus.service.ai.dataset.Records.Student;
import com.campus.smartcampus.service.ai.dataset.Records.TimetableEntry;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Hand-picked sample data for the AI track.
 *
 * <p>The fixtures are rigged on purpose so every algorithm produces a visible
 * result in the Day-10 demo: at least one HIGH-risk student, one room that
 * clearly wins a recommendation, one notice that clearly outranks the rest,
 * and one account showing an obvious brute-force spike.
 *
 * <p>Frozen once written. Tests and the demo read from this class so their
 * output can never drift apart.
 */
public final class DataSeeder {

    /** Frozen base time. Tests and demo both use this. */
    public static final LocalDate DEMO_DATE = LocalDate.of(2026, 10, 5);

    private static final DateTimeFormatter TS = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private DataSeeder() {
    }

    // ---------- Departments ----------

    public static List<Department> departments() {
        return List.of(
                new Department("D1", "CSE", "Computer Science and Engineering"),
                new Department("D2", "ECE", "Electronics and Communication"),
                new Department("D3", "MEC", "Mechanical Engineering")
        );
    }

    // ---------- Courses ----------

    public static List<Course> courses() {
        return List.of(
                new Course("C1", "CS101", "Programming Fundamentals", 4, "D1", 60),
                new Course("C2", "CS201", "Data Structures", 4, "D1", 60),
                new Course("C3", "CS301", "Database Systems", 3, "D1", 100)
        );
    }

    /** CS301 is the recommendation demo case: 100 capacity, needs a projector. */
    public static Course demoCourse() {
        return courses().get(2);
    }

    // ---------- Students ----------

    public static List<Student> students() {
        List<Student> out = new ArrayList<>();
        out.add(new Student("S1", "Anitha Raghavan", "anitha@smartcampus.edu", "D1"));
        out.add(new Student("S2", "Bala Murugan", "bala@smartcampus.edu", "D1"));
        out.add(new Student("S3", "Chitra Devi", "chitra@smartcampus.edu", "D1"));
        out.add(new Student("S4", "Deepak Kumar", "deepak@smartcampus.edu", "D1"));
        out.add(new Student("S5", "Eswari Menon", "eswari@smartcampus.edu", "D1"));
        out.add(new Student("S6", "Farhan Ali", "farhan@smartcampus.edu", "D1"));
        out.add(new Student("S7", "Gayathri Iyer", "gayathri@smartcampus.edu", "D2"));
        out.add(new Student("S8", "Hari Krishnan", "hari@smartcampus.edu", "D2"));
        out.add(new Student("S9", "Indira Nair", "indira@smartcampus.edu", "D2"));
        out.add(new Student("S10", "Jagan Pillai", "jagan@smartcampus.edu", "D2"));
        out.add(new Student("S11", "Kavitha Raj", "kavitha@smartcampus.edu", "D2"));
        out.add(new Student("S12", "Lakshmi Devi", "lakshmi@smartcampus.edu", "D3"));
        out.add(new Student("S13", "Manoj Verma", "manoj@smartcampus.edu", "D3"));
        out.add(new Student("S14", "Nithya Sri", "nithya@smartcampus.edu", "D3"));
        out.add(new Student("S15", "Om Prakash", "om@smartcampus.edu", "D3"));
        out.add(new Student("S16", "Priya Sharma", "priya@smartcampus.edu", "D3"));
        out.add(new Student("S17", "Qadir Hussain", "qadir@smartcampus.edu", "D3"));
        out.add(new Student("S18", "Ramesh Babu", "ramesh@smartcampus.edu", "D1"));
        out.add(new Student("S19", "Saranya Vivek", "saranya@smartcampus.edu", "D1"));
        out.add(new Student("S20", "Tamil Selvi", "tamil@smartcampus.edu", "D1"));
        return out;
    }

    /**
     * Attendance histories. Each string is a status pattern, P=present,
     * A=absent, L=late, E=excused. S1's pattern is the 75% boundary case.
     */
    public static List<Attendance> attendance() {
        String[][] specs = {
                {"S1", "PPPA"},   // 75.0% -> MEDIUM (boundary)
                {"S2", "PPPP"},   // 100%   -> LOW
                {"S3", "AAAP"},   // 25%    -> HIGH
                {"S4", "PPAPP"},  // 80%
                {"S5", "PPPAP"},  // 80%
                {"S6", "AAPP"},   // 50%
                {"S7", "PPPAP"},  // 80%
                {"S8", "PPPPA"},  // 80%
                {"S9", "PPLL"},   // 100% counting LATE as present
                {"S10", "AAAP"},  // 25% -> HIGH
                {"S11", "PPPPP"}, // 100%
                {"S12", "PAAP"},  // 50%
                {"S13", "PPPPA"}, // 80%
                {"S14", "PPPAA"}, // 60%
                {"S15", "PPPP"},  // 100%
                {"S16", "APPP"},  // 75% -> MEDIUM
                {"S17", "PPPPA"}, // 80%
                {"S18", "PPPP"},  // 100%
                {"S19", "PPAAP"}, // 75% -> MEDIUM
                {"S20", "AAAA"}   // 0%  -> HIGH
        };

        List<Attendance> out = new ArrayList<>();
        int seq = 1;
        for (String[] spec : specs) {
            String studentId = spec[0];
            String pattern = spec[1];
            for (int i = 0; i < pattern.length(); i++) {
                AttendanceStatus status = switch (pattern.charAt(i)) {
                    case 'P' -> AttendanceStatus.PRESENT;
                    case 'A' -> AttendanceStatus.ABSENT;
                    case 'L' -> AttendanceStatus.LATE;
                    case 'E' -> AttendanceStatus.EXCUSED;
                    default -> AttendanceStatus.PRESENT;
                };
                LocalDate date = LocalDate.of(2026, 9, 1).plusDays(i);
                out.add(new Attendance("A" + seq++, studentId, "C1", date, status));
            }
        }
        return out;
    }

    /** S1, S2 and S3 are the demo calls in the attendance-risk section. */
    public static List<Attendance> historyFor(String studentId) {
        List<Attendance> out = new ArrayList<>();
        for (Attendance a : attendance()) {
            if (a.studentId().equals(studentId)) {
                out.add(a);
            }
        }
        return out;
    }

    public static Student student(String id) {
        return students().stream()
                .filter(s -> s.id().equals(id))
                .findFirst()
                .orElseThrow();
    }

    // ---------- Rooms ----------

    /**
     * For a 100-capacity course needing a projector, LH-301 should win:
     * it is the smallest room that still fits with a projector.
     */
    public static List<Room> rooms() {
        return List.of(
                new Room("R1", "LH-301", "Lecture Hall 301", RoomType.LECTURE_HALL, 120, 3,
                        true, true, true),
                new Room("R2", "LH-305", "Lecture Hall 305", RoomType.LECTURE_HALL, 100, 3,
                        false, false, true),
                new Room("R3", "AUD-01", "Main Auditorium", RoomType.AUDITORIUM, 500, 1,
                        true, true, true),
                new Room("R4", "LAB-102", "Programming Lab 102", RoomType.LABORATORY, 40, 1,
                        true, true, true),
                new Room("R5", "SR-12", "Seminar Room 12", RoomType.SEMINAR_ROOM, 30, 2,
                        true, false, true),
                new Room("R6", "CL-01", "Computer Lab 01", RoomType.COMPUTER_LAB, 60, 2,
                        true, true, true),
                new Room("R7", "CR-05", "Conference Room 05", RoomType.CONFERENCE_ROOM, 15, 4,
                        true, true, true),
                new Room("R8", "LH-401", "Lecture Hall 401", RoomType.LECTURE_HALL, 80, 4,
                        true, false, true)
        );
    }

    // ---------- Bookings (with a deliberate overlap) ----------

    /** CS301's slot on demo day, Mon 09:00-10:00, in LH-301. */
    public static final LocalDateTime CS301_SLOT_START = LocalDateTime.of(2026, 10, 5, 9, 0);
    public static final LocalDateTime CS301_SLOT_END = LocalDateTime.of(2026, 10, 5, 10, 0);

    public static List<RoomBooking> bookings() {
        return List.of(
                // LH-301 is already taken 09:00-10:00, overlapping CS301's slot
                new RoomBooking("B1", "R1", "faculty@smartcampus.edu",
                        "CS201 class", CS301_SLOT_START, CS301_SLOT_END, "CONFIRMED"),
                // LH-305 is free at that time
                new RoomBooking("B2", "R2", "library@smartcampus.edu",
                        "Book review", LocalDateTime.of(2026, 10, 5, 14, 0),
                        LocalDateTime.of(2026, 10, 5, 15, 0), "CONFIRMED"),
                new RoomBooking("B3", "R6", "student@smartcampus.edu",
                        "Project meeting", LocalDateTime.of(2026, 10, 5, 11, 0),
                        LocalDateTime.of(2026, 10, 5, 12, 0), "PENDING")
        );
    }

    // ---------- Timetable ----------

    public static List<TimetableEntry> timetable() {
        return List.of(
                new TimetableEntry("T1", "C1", "R1", "F1",
                        DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)),
                new TimetableEntry("T2", "C2", "R6", "F1",
                        DayOfWeek.MONDAY, LocalTime.of(10, 0), LocalTime.of(11, 0)),
                new TimetableEntry("T3", "C1", "R1", "F2",
                        DayOfWeek.WEDNESDAY, LocalTime.of(9, 0), LocalTime.of(10, 0)),
                new TimetableEntry("T4", "C3", "R2", "F1",
                        DayOfWeek.FRIDAY, LocalTime.of(14, 0), LocalTime.of(15, 0)),
                new TimetableEntry("T5", "C2", "R6", "F2",
                        DayOfWeek.TUESDAY, LocalTime.of(12, 0), LocalTime.of(13, 0))
        );
    }

    /** F1: faculty whose Monday schedule is already partly full. */
    public static final String DEMO_FACULTY_ID = "F1";

    // ---------- Notices ----------

    public static List<Notice> notices() {
        return List.of(
                new Notice("N1", "Campus closed tomorrow - cyclone warning",
                        "All classes cancelled due to severe weather alert.",
                        NoticeCategory.EMERGENCY, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 10, 4, 8, 0),
                        LocalDateTime.of(2026, 10, 20, 23, 59), "ALL"),
                new Notice("N2", "End semester examination schedule released",
                        "Timetable available on the student portal from Monday.",
                        NoticeCategory.EXAMINATION, "admin@smartcampus.edu",
                        true, true, LocalDateTime.of(2026, 9, 28, 12, 0),
                        LocalDateTime.of(2026, 11, 30, 23, 59), "ALL"),
                new Notice("N3", "Revised syllabus for CS301 Database Systems",
                        "Unit 4 replaced with indexing and query optimisation.",
                        NoticeCategory.ACADEMIC, "faculty@smartcampus.edu",
                        true, true, LocalDateTime.of(2026, 10, 1, 10, 0),
                        LocalDateTime.of(2026, 12, 31, 23, 59), "STUDENT"),
                new Notice("N4", "Fee payment deadline extended to Oct 15",
                        "Pay semester fees through the portal before the new deadline.",
                        NoticeCategory.ADMINISTRATIVE, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 25, 9, 30),
                        LocalDateTime.of(2026, 10, 15, 23, 59), "STUDENT"),
                new Notice("N5", "Annual tech fest registrations now open",
                        "Register your team before Oct 10 for Hackathon 2026.",
                        NoticeCategory.EVENT, "student@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 20, 15, 0),
                        LocalDateTime.of(2026, 10, 10, 23, 59), "ALL"),
                new Notice("N6", "TCS campus drive this Friday",
                        "Eligible final-year students must register on the placement cell.",
                        NoticeCategory.PLACEMENT, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 10, 3, 14, 0),
                        LocalDateTime.of(2026, 10, 12, 23, 59), "STUDENT"),
                new Notice("N7", "Inter-college cricket trials",
                        "Selection trials on Saturday morning at the main ground.",
                        NoticeCategory.SPORTS, "faculty@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 18, 11, 0),
                        LocalDateTime.of(2026, 10, 8, 23, 59), "ALL"),
                new Notice("N8", "Cultural night auditions",
                        "Auditions for dance and music on Wednesday after class hours.",
                        NoticeCategory.CULTURAL, "student@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 22, 16, 0),
                        LocalDateTime.of(2026, 10, 9, 23, 59), "ALL"),
                new Notice("N9", "Library timings changed during exams",
                        "Library will remain open until 22:00 from next week.",
                        NoticeCategory.GENERAL, "admin@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 9, 15, 9, 0),
                        LocalDateTime.of(2026, 11, 10, 23, 59), "ALL"),
                new Notice("N10", "Old bus route notice (EXPIRED)",
                        "This notice expired and must be dropped from the feed.",
                        NoticeCategory.GENERAL, "transport@smartcampus.edu",
                        false, true, LocalDateTime.of(2026, 8, 1, 9, 0),
                        LocalDateTime.of(2026, 8, 15, 23, 59), "ALL")
        );
    }

    /** Reference "now" for prioritisation. N10 is intentionally expired vs this. */
    public static final LocalDateTime DEMO_NOW = LocalDateTime.of(2026, 10, 5, 9, 30);

    // ---------- Login events ----------

    /**
     * brute@smartcampus.edu fails 4 times inside 5 minutes from one IP.
     * indira@smartcampus.edu logs in at 03:10 (off hours).
     */
    public static List<LoginEvent> loginEvents() {
        return List.of(
                new LoginEvent("L1", "bala@smartcampus.edu", "10.0.0.5",
                        LocalDateTime.of(2026, 10, 5, 8, 30), true),
                new LoginEvent("L2", "anitha@smartcampus.edu", "10.0.0.9",
                        LocalDateTime.of(2026, 10, 5, 8, 45), true),
                new LoginEvent("L3", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 10), false),
                new LoginEvent("L4", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 12), false),
                new LoginEvent("L5", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 14), false),
                new LoginEvent("L6", "brute@smartcampus.edu", "203.0.113.7",
                        LocalDateTime.of(2026, 10, 5, 2, 15), false),
                new LoginEvent("L7", "indira@smartcampus.edu", "10.0.0.21",
                        LocalDateTime.of(2026, 10, 5, 3, 10), true),
                new LoginEvent("L8", "chitra@smartcampus.edu", "10.0.0.14",
                        LocalDateTime.of(2026, 10, 5, 9, 5), false),
                new LoginEvent("L9", "deepak@smartcampus.edu", "10.0.0.33",
                        LocalDateTime.of(2026, 10, 5, 9, 15), true),
                new LoginEvent("L10", "eswari@smartcampus.edu", "10.0.0.41",
                        LocalDateTime.of(2026, 10, 5, 9, 25), true)
        );
    }

    /** Helper used by tests and the demo to render timestamps consistently. */
    public static String fmt(LocalDateTime t) {
        return t.format(TS);
    }
}
