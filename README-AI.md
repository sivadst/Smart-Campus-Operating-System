# Smart Campus — AI Track (Rule-Based Algorithms)

Person 2's "AI" track for a 3rd-semester Java project. Six deterministic Java algorithm classes in `com.campus.smartcampus.service.ai`, a deliberately-rigged dataset (`DataSeeder`), and a demo entry point (`Main.java`). Every algorithm uses thresholds, weighted scoring, if-rules, or aggregation — **no machine learning**.

---

> **⚠️ This is rule-based Java, not machine learning.**  
> All decisions come from explicit constants and formulas (e.g., `>= 90 → LOW`, `capacityFit = max(0, 10 - wastedSeats/10)`). There is no training data, no model fitting, and no ML libraries on the classpath.

---

## Build & Run

### Without Maven (javac/java)
```bash
mkdir -p out
javac -d out $(find src -name '*.java')
java -cp out com.campus.smartcampus.Main
```

### With Maven (wrapper)
```bash
./mvnw test          # runs unit tests
./mvnw compile exec:java -Dexec.mainClass=com.campus.smartcampus.Main
```

---

## Algorithms

### 1. AttendanceRiskPredictor
**Package:** `com.campus.smartcampus.service.ai`

**Signatures**
```java
static double attendancePercentage(List<Records.Attendance> history)
static String risk(List<Records.Attendance> history)           // "LOW" | "MEDIUM" | "HIGH"
static String explain(List<Records.Attendance> history)
```

**Rule (plain English)**
- Attendance % = (PRESENT + LATE) / total classes × 100
- ≥ 90% → LOW risk
- ≥ 75% → MEDIUM risk
- < 75% → HIGH risk
- Empty history → HIGH risk

**Verified examples**
| Student | Pattern | Attended | Total | % | Risk |
|---------|---------|----------|-------|----|------|
| S1 | PPPA | 3 | 4 | 75.0 | **MEDIUM** (boundary) |
| S2 | PPPP | 4 | 4 | 100.0 | **LOW** |
| S3 | AAAP | 1 | 4 | 25.0 | **HIGH** |
| (empty) | — | 0 | 0 | 0.0 | **HIGH** |

---

### 2. TimetableSuggester
**Package:** `com.campus.smartcampus.service.ai`

**Signature**
```java
static List<SuggestedSlot> suggest(
    Records.Course course,
    String facultyId,
    List<Records.Room> rooms,
    List<Records.TimetableEntry> existing,
    int limit
) -> List<SuggestedSlot>
```
`SuggestedSlot(DayOfWeek day, LocalTime start, LocalTime end, String roomId, String reason)`

**Rule (plain English)**
- Greedy first-fit scan over fixed 1-hour slots:
  - Days: Mon–Fri
  - Slot starts: 08:00, 09:00, 10:00, **11:00 & 12:00 skipped (lunch)**, 14:00, 15:00, 16:00
- For each slot, pick the **first room in the provided list** that:
  1. Has capacity ≥ course capacity
  2. Has no room clash (same room, overlapping time, same day)
  3. Has no faculty clash (same faculty, overlapping time, same day)
- Return up to `limit` slots in deterministic scan order.

**Verified example**
- Course: CS301 (capacity 100)
- Faculty: F1 with existing entries T1 (R1, MON 09:00–10:00) and T2 (R6, MON 10:00–11:00)
- Rooms: R1 (120), R2 (100), R6 (60)
- **First 3 valid MONDAY slots**:  
  1. 08:00–09:00 (R1 or R2)  
  2. 14:00–15:00 (R1 or R2)  
  3. 15:00–16:00 (R1 or R2)  
  *(11:00 and 12:00 are skipped for lunch)*

---

### 3. RoomRecommender
**Package:** `com.campus.smartcampus.service.ai`

**Signature**
```java
static List<RoomScore> recommend(
    Records.Course course,
    boolean needsProjector,
    List<Records.Room> rooms,
    List<Records.RoomBooking> bookings,
    LocalDateTime slotStart,
    LocalDateTime slotEnd,
    int limit
) -> List<RoomScore>
```
`RoomScore(Records.Room room, double score, String reason)`

**Rule (plain English)**
1. **Disqualify** rooms with capacity < course capacity.
2. **Capacity fit**: `max(0, 10 - (room.capacity - course.capacity) / 10)`  
   → rewards rooms sized close to the class.
3. **Amenity match**:
   - Projector (if needed): +3
   - AC: +1
   - WiFi: +1
4. **Availability bonus**: +5 if no CONFIRMED booking overlaps the slot.
5. **Total score** = capacityFit + amenityMatch + availabilityBonus.
6. Sort descending by score, then by room ID for determinism. Return top `limit`.

**Verified example**
- Course: CS301 (capacity 100, needsProjector=true)
- Slot: 2026-10-05T09:00–10:00
- Bookings: B1 locks R1 (LH-301) at that slot (CONFIRMED)

| Room | Capacity | CapacityFit | Projector | AC | WiFi | Free? | Total |
|------|----------|-------------|-----------|----|------|-------|-------|
| **LH-305 (R2)** | 100 | 10.0 | 0 | 0 | +1 | **+5** | **16.0** ← winner |
| LH-301 (R1) | 120 | 8.0 | +3 | +1 | +1 | 0 (booked) | 13.0 |
| AUD-01 (R3) | 500 | 0.0 | +3 | +1 | +1 | +5 | 10.0 |
| LAB-102 (R4) | 40 | **disqualified** | — | — | — | — | — |
| SR-12 (R5) | 30 | **disqualified** | — | — | — | — | — |
| CL-01 (R6) | 60 | **disqualified** | — | — | — | — | — |
| CR-05 (R7) | 15 | **disqualified** | — | — | — | — | — |
| LH-401 (R8) | 80 | **disqualified** | — | — | — | — | — |

---

### 4. NoticePrioritizer
**Package:** `com.campus.smartcampus.service.ai`

**Signature**
```java
static List<ScoredNotice> rank(
    List<Records.Notice> notices,
    LocalDateTime now,
    String audienceRole,
    int limit
) -> List<ScoredNotice>
```
`ScoredNotice(Notice notice, double score, String reason)`

**Rule (plain English)**
1. **Drop** unpublished notices.
2. **Drop** expired notices (`expiresAt != null && now.isAfter(expiresAt)`).
3. **Score** = sum of:
   - **Category weight**: EMERGENCY 5, EXAMINATION 4, ACADEMIC 3, ADMINISTRATIVE/PLACEMENT/EVENT 2, SPORTS/CULTURAL/GENERAL 1
   - **Pin bonus**: +4 if `pinned == true`
   - **Recency**: `max(0, 3 - daysSincePublished)` (3 for today, 2 for 1 day old, 1 for 2 days old, 0 for 3+)
   - **Audience match**: +2 if `targetRole == "ALL"` or matches `audienceRole`
   - **Expiry penalty**: -1 if expires within 3 days (`0 ≤ daysUntilExpiry ≤ 3`)
4. Sort descending by score, then by notice ID. Return top `limit`.

**Verified example** (`now = 2026-10-05T09:30`, audience = STUDENT)
| Notice | Category | Pin | DaysOld | Audience | ExpiresSoon | Score |
|--------|----------|-----|---------|----------|-------------|-------|
| **N2** | EXAMINATION (4) | +4 | 7→0 | ALL (+2) | no | **10.0** |
| **N1** | EMERGENCY (5) | 0 | 1→2 | ALL (+2) | no | **9.0** |
| **N3** | ACADEMIC (3) | +4 | 4→0 | STUDENT (+2) | no | **9.0** |
| N4 | ADMIN (2) | 0 | 10→0 | STUDENT (+2) | **yes (-1)** | 3.0 |
| N5 | EVENT (2) | 0 | 15→0 | ALL (+2) | yes (-1) | 3.0 |
| N6 | PLACEMENT (2) | 0 | 2→1 | STUDENT (+2) | yes (-1) | 4.0 |
| N7 | SPORTS (1) | 0 | 17→0 | ALL (+2) | yes (-1) | 2.0 |
| N8 | CULTURAL (1) | 0 | 13→0 | ALL (+2) | yes (-1) | 2.0 |
| N9 | GENERAL (1) | 0 | 20→0 | ALL (+2) | no | 3.0 |
| **N10** | GENERAL (1) | 0 | — | ALL (+2) | — | **DROPPED (expired)** |

- 10 notices in → 9 survive → top 3: **N2, N1, N3**

---

### 5. SecurityAnalyser
**Package:** `com.campus.smartcampus.service.ai`

**Signature**
```java
static List<Finding> flag(List<Records.LoginEvent> events) -> List<Finding>
```
`Finding(String severity, String type, String subject, String detail)`

**Rule (plain English)** — evaluated per email:
1. **BRUTE_FORCE_SUSPECTED** (HIGH): ≥ 3 failures in any rolling 5-minute window.
2. **OFF_HOURS_ACCESS** (MEDIUM): successful login between 01:00–04:59 (failures don't count).
3. **REPEATED_FAILURES** (HIGH): ≥ 5 total failures for that email.
4. **MULTIPLE_IPS** (LOW): successful logins from ≥ 3 distinct IPs.
5. Results sorted: HIGH → MEDIUM → LOW, then subject (email) ascending.

**Verified example**
| Email | Findings |
|-------|----------|
| **brute@smartcampus.edu** | 4 failures at 02:10, 02:12, 02:14, 02:15 (all within 5 min) → **BRUTE_FORCE_SUSPECTED (HIGH)** |
| **indira@smartcampus.edu** | success at 03:10 → **OFF_HOURS_ACCESS (MEDIUM)** |
| chitra@smartcampus.edu | 1 failure only → *(no finding)* |
| Others | clean |

Output: **2 findings** — 1 HIGH, 1 MEDIUM

---

### 6. InsightsService
**Package:** `com.campus.smartcampus.service.ai`

**Signature**
```java
static CampusInsights compute(
    List<Records.Student> students,
    List<Records.Attendance> attendance,
    List<Records.Room> rooms,
    List<Records.RoomBooking> bookings,
    List<Records.TimetableEntry> timetable,
    List<Records.Notice> notices
) -> CampusInsights
```
`CampusInsights(double averageAttendance, int highRiskStudents, double roomUtilisationPct, Map<String,Long> noticesByCategory, List<String> topRooms)`

**Rule (plain English)**
- **averageAttendance**: (PRESENT + LATE) / total attendance records × 100
- **highRiskStudents**: count of students with personal attendance % < 75% (and > 0 classes)
- **roomUtilisationPct**: distinct rooms with at least one booking / total rooms × 100
- **noticesByCategory**: count of published, non-expired notices grouped by category
- **topRooms**: top 5 rooms by timetable entry count (descending)

**Verified output** (from demo dataset)
| Metric | Value |
|--------|-------|
| Average attendance | **70.79%** |
| High-risk students | **7** |
| Room utilisation | **37.5%** (3 of 8 rooms booked) |
| Notice categories | **7** (EMERGENCY, EXAMINATION, ACADEMIC, ADMINISTRATIVE, EVENT, PLACEMENT, SPORTS, CULTURAL, GENERAL → 9 categories but only 7 have published non-expired) |
| Top rooms | **[R1, R6, R2]** (R1=2, R6=2, R2=1) |

---

## Demo Output Table

| Section | Output |
|---------|--------|
| Attendance Risk | `MEDIUM risk for S1` |
| Timetable Suggester | `3 valid slots found` |
| Room Recommender | `1 winner (LH-305) with score 16.0` |
| Notice Prioritizer | `Top 3 notices: N2, N1, N3` |
| Security Analyser | `2 findings: 1 HIGH, 1 MEDIUM` |

Full captured run:
```
=== ATTENDANCE RISK ===
Result: MEDIUM risk for S1

=== TIMETABLE SUGGESTER ===
Result: 3 valid slots found

=== ROOM RECOMMENDER ===
Result: 1 winner (LH-305) with score 16.0

=== NOTICE PRIORITIZER ===
Result: Top 3 notices: N2, N1, N3

=== SECURITY ANALYSER ===
Result: 2 findings: 1 HIGH, 1 MEDIUM

=== DEMO COMPLETE ===
```

---

## Extending — Swapping DataSeeder for Real Data

All six algorithms accept **plain Java records** (`Records.Attendance`, `Records.Room`, `Records.Notice`, etc.) — no framework types, no JPA entities. To wire a real database:

1. **Keep the algorithm classes unchanged** — they are pure functions over the `Records` types.
2. **Implement a repository layer** that reads from your DB and maps rows to `Records.*` records.
3. **Replace `DataSeeder.*` calls** in `Main.java` (or your service layer) with calls to your repositories.
4. **No signature changes needed** — the method signatures are stable:
   - `AttendanceRiskPredictor.risk(List<Attendance>)`
   - `TimetableSuggester.suggest(Course, String, List<Room>, List<TimetableEntry>, int)`
   - `RoomRecommender.recommend(Course, boolean, List<Room>, List<RoomBooking>, LocalDateTime, LocalDateTime, int)`
   - `NoticePrioritizer.rank(List<Notice>, LocalDateTime, String, int)`
   - `SecurityAnalyser.flag(List<LoginEvent>)`
   - `InsightsService.compute(...)`

Example sketch:
```java
// Instead of DataSeeder.attendance()
List<Records.Attendance> history = attendanceRepository.findByStudentId(studentId);

// Instead of DataSeeder.rooms()
List<Records.Room> rooms = roomRepository.findAll();

// Instead of DataSeeder.loginEvents()
List<Records.LoginEvent> events = loginEventRepository.findSince(Instant.now().minusDays(7));
```

Because the algorithms have **zero dependencies** beyond `java.time` and `java.util`, they drop into Spring, Quarkus, Micronaut, or a plain servlet container without changes.

---

## Repo Contribution Rules (from upstream CONTRIBUTING.md)

- **Java 21** — use `var`, records, pattern matching, `List.of`, etc.
- **Conventional Commits** — `feat:`, `fix:`, `docs:`, `test:`, `refactor:`, `chore:`
- **Branch off `main`** — feature branches, PRs with clear description
- **Write tests** — tests must pass (`./mvnw test`)
- **No source modifications** to algorithm classes without a test update

---

*Generated from verified source — demo output captured from a real compile/run.*