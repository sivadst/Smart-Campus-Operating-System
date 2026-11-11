package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Greedy rule-based timetable slot suggester.
 *
 * <p>This is a deterministic first-fit search over predefined one-hour blocks.
 * It is <strong>not</strong> a machine learning model; it applies simple
 * constraint checks (room clash, faculty clash, capacity) and returns the
 * first {@code limit} viable slots.
 */
public final class TimetableSuggester {

    private TimetableSuggester() {
    }

    /** A free, conflict-free one-hour slot for a course on a given weekday. */
    public record SuggestedSlot(DayOfWeek day, LocalTime start, LocalTime end,
                                 String roomId, String reason) {
    }

    /**
     * Greedy suggestion: scan slots and return the first {@code limit} that have
     * no room clash and no faculty clash.
     *
     * @param course     the course needing a slot
     * @param facultyId  the faculty member's ID
     * @param rooms      available rooms (checked in list order)
     * @param existing   current timetable entries
     * @param limit      maximum slots to return
     * @return up to {@code limit} conflict-free slots, in deterministic order
     */
    public static List<SuggestedSlot> suggest(Records.Course course, String facultyId,
                                               List<Records.Room> rooms,
                                               List<Records.TimetableEntry> existing,
                                               int limit) {
        List<SuggestedSlot> result = new ArrayList<>();

        DayOfWeek[] days = {DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY,
                            DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
        LocalTime[] starts = {LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0),
                              LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0)};

        for (DayOfWeek day : days) {
            for (LocalTime start : starts) {
                if (result.size() >= limit) {
                    return result;
                }
                LocalTime end = start.plusHours(1);

                // Find first suitable room
                for (Records.Room room : rooms) {
                    if (room.capacity() < course.capacity()) {
                        continue;
                    }
                    if (isFree(room.id(), facultyId, day, start, end, existing)) {
                        result.add(new SuggestedSlot(day, start, end, room.id(),
                                                     "faculty free, room free"));
                        break;
                    }
                }
            }
        }
        return result;
    }

    private static boolean isFree(String roomId, String facultyId, DayOfWeek day,
                                  LocalTime start, LocalTime end,
                                  List<Records.TimetableEntry> existing) {
        for (Records.TimetableEntry e : existing) {
            if (e.day() != day) {
                continue;
            }
            boolean timeOverlap = start.isBefore(e.end()) && e.start().isBefore(end);
            if (!timeOverlap) {
                continue;
            }
            if (e.roomId().equals(roomId) || e.facultyId().equals(facultyId)) {
                return false;
            }
        }
        return true;
    }
}