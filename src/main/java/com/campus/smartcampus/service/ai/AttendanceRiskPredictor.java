package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import java.util.List;

/**
 * Rule-based attendance risk predictor.
 *
 * <p>This class provides simple deterministic rules for assessing student attendance risk.
 * It is <strong>not</strong> a machine learning model — thresholds are fixed and
 * explicitly defined for transparency and auditability.
 */
public final class AttendanceRiskPredictor {

    private AttendanceRiskPredictor() {
    }

    /**
     * Calculates the percentage of classes attended.
     *
     * <p>Counts {@code PRESENT} and {@code LATE} as attended.
     * Returns {@code 0.0} for an empty history.
     *
     * @param history list of attendance records
     * @return attendance percentage (0.0 to 100.0)
     */
    public static double attendancePercentage(List<Records.Attendance> history) {
        if (history == null || history.isEmpty()) {
            return 0.0;
        }
        long attended = history.stream()
                .filter(a -> a.status() == Records.AttendanceStatus.PRESENT
                        || a.status() == Records.AttendanceStatus.LATE)
                .count();
        return 100.0 * attended / history.size();
    }

    /**
     * Determines the risk level based on attendance percentage.
     *
     * <p>Thresholds (inclusive boundaries):
     * <ul>
     *   <li>{@code >= 90.0} -> "LOW"</li>
     *   <li>{@code >= 75.0} -> "MEDIUM"</li>
     *   <li>otherwise -> "HIGH"</li>
     * </ul>
     * Returns {@code "HIGH"} for an empty history.
     *
     * @param history list of attendance records
     * @return risk level: "LOW", "MEDIUM", or "HIGH"
     */
    public static String risk(List<Records.Attendance> history) {
        double pct = attendancePercentage(history);
        if (pct >= 90.0) {
            return "LOW";
        }
        if (pct >= 75.0) {
            return "MEDIUM";
        }
        return "HIGH";
    }

    /**
     * Returns a short human-readable explanation for the demo.
     *
     * <p>Format: {@code "75.0% attended over 4 classes"} (one decimal place).
     * Returns {@code "no records"} for an empty history.
     *
     * @param history list of attendance records
     * @return explanation string
     */
    public static String explain(List<Records.Attendance> history) {
        if (history == null || history.isEmpty()) {
            return "no records";
        }
        double pct = attendancePercentage(history);
        return String.format("%.1f%% attended over %d classes", pct, history.size());
    }
}