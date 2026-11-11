package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Rule-based room recommender using weighted additive scoring.
 *
 * <p>This is a deterministic scoring system, <strong>not</strong> a machine learning model.
 * It evaluates rooms for a given course and time slot based on capacity fit,
 * amenity match, and availability, then ranks them highest-score first.
 */
public final class RoomRecommender {

    private RoomRecommender() {
    }

    /** Score for one room for a course; higher is better. */
    public record RoomScore(Records.Room room, double score, String reason) {
    }

    // Scoring constants
    private static final double CAPACITY_FIT_WEIGHT = 10.0;
    private static final double AMENITY_PROJECTOR_BONUS = 3.0;
    private static final double AMENITY_AC_BONUS = 1.0;
    private static final double AMENITY_WIFI_BONUS = 1.0;
    private static final double AVAILABILITY_BONUS = 5.0;

    /**
     * Rank rooms best-first for a course needing a 1-hour slot at slotStart.
     *
     * @param course       the course needing a room
     * @param needsProjector whether the course requires a projector
     * @param rooms        available rooms
     * @param bookings     existing room bookings
     * @param slotStart    start of the desired time slot
     * @param slotEnd      end of the desired time slot
     * @param limit        maximum number of recommendations to return
     * @return up to {@code limit} rooms ranked by score (descending)
     */
    public static List<RoomScore> recommend(Records.Course course, boolean needsProjector,
                                             List<Records.Room> rooms,
                                             List<Records.RoomBooking> bookings,
                                             LocalDateTime slotStart,
                                             LocalDateTime slotEnd,
                                             int limit) {
        List<RoomScore> scores = new ArrayList<>();

        for (Records.Room room : rooms) {
            // Capacity check - disqualify if too small
            if (room.capacity() < course.capacity()) {
                continue;
            }

            // Capacity fit: reward rooms sized close to class size
            double capacityFit = Math.max(0, CAPACITY_FIT_WEIGHT
                - (room.capacity() - course.capacity()) / 10.0);

            // Amenity match
            double amenityMatch = 0;
            StringBuilder reason = new StringBuilder();
            reason.append(room.capacity()).append(" seats for ").append(course.capacity())
                  .append(" needed (").append(String.format("%.1f", capacityFit)).append(")");

            if (needsProjector && room.hasProjector()) {
                amenityMatch += AMENITY_PROJECTOR_BONUS;
                reason.append(", projector (").append(AMENITY_PROJECTOR_BONUS).append(")");
            }
            if (room.hasAc()) {
                amenityMatch += AMENITY_AC_BONUS;
                reason.append(", AC (").append(AMENITY_AC_BONUS).append(")");
            }
            if (room.hasWifi()) {
                amenityMatch += AMENITY_WIFI_BONUS;
                reason.append(", WiFi (").append(AMENITY_WIFI_BONUS).append(")");
            }

            // Availability bonus
            double availabilityBonus = 0;
            boolean isFree = true;
            for (Records.RoomBooking b : bookings) {
                if (b.status().equalsIgnoreCase("CONFIRMED")
                    && b.start().isBefore(slotEnd)
                    && slotStart.isBefore(b.end())
                    && b.roomId().equals(room.id())) {
                    isFree = false;
                    break;
                }
            }
            if (isFree) {
                availabilityBonus = AVAILABILITY_BONUS;
                reason.append(", free at slot (").append(AVAILABILITY_BONUS).append(")");
            } else {
                reason.append(", booked at slot (0)");
            }

            double totalScore = capacityFit + amenityMatch + availabilityBonus;
            scores.add(new RoomScore(room, totalScore, reason.toString()));
        }

        // Sort descending by score, then by room id for determinism
        scores.sort(Comparator
            .comparingDouble((RoomScore rs) -> -rs.score())
            .thenComparing(rs -> rs.room().id()));

        // Return top limit
        return scores.stream().limit(limit).toList();
    }
}