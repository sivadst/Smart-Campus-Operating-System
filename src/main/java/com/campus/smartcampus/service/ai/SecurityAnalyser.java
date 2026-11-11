package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records.LoginEvent;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Rule-based anomaly detection over a login-event log.
 * <p>
 * This class applies deterministic if/else rules (no machine learning) to flag
 * suspicious authentication patterns such as brute-force attempts, off-hours
 * access, repeated failures, and multi-IP logins.
 */
public final class SecurityAnalyser {

    private SecurityAnalyser() {}

    /** A single security finding. */
    public record Finding(String severity, String type, String subject, String detail) {}

    // Threshold constants
    private static final int BRUTE_FORCE_THRESHOLD = 3;
    private static final int BRUTE_FORCE_WINDOW_MINUTES = 5;
    private static final int OFF_HOURS_START_HOUR = 1;
    private static final int OFF_HOURS_END_HOUR = 5;
    private static final int REPEATED_FAILURES_THRESHOLD = 5;
    private static final int MULTI_IP_THRESHOLD = 3;

    /**
     * Analyses login events and returns a list of security findings.
     *
     * @param events the login events to analyse
     * @return sorted, deduplicated list of findings
     */
    public static List<Finding> flag(List<LoginEvent> events) {
        if (events == null || events.isEmpty()) return List.of();

        Set<Finding> findings = new LinkedHashSet<>();

        Map<String, List<LoginEvent>> byEmail = events.stream()
                .collect(Collectors.groupingBy(LoginEvent::email));

        for (var entry : byEmail.entrySet()) {
            String email = entry.getKey();
            List<LoginEvent> userEvents = entry.getValue();

            List<LoginEvent> successes = userEvents.stream()
                    .filter(LoginEvent::success)
                    .toList();
            List<LoginEvent> failures = userEvents.stream()
                    .filter(e -> !e.success())
                    .toList();

            // Rule 1: Brute force - 3+ failures in any rolling 5-minute window
            if (failures.size() >= BRUTE_FORCE_THRESHOLD) {
                List<LocalDateTime> failureTimes = failures.stream()
                        .map(LoginEvent::timestamp)
                        .sorted()
                        .toList();
                String firstIp = failures.get(0).ip();
                for (int i = 0; i <= failureTimes.size() - BRUTE_FORCE_THRESHOLD; i++) {
                    LocalDateTime windowStart = failureTimes.get(i);
                    LocalDateTime windowEnd = windowStart.plusMinutes(BRUTE_FORCE_WINDOW_MINUTES);
                    long countInWindow = failureTimes.stream()
                            .filter(t -> !t.isBefore(windowStart) && !t.isAfter(windowEnd))
                            .count();
                    if (countInWindow >= BRUTE_FORCE_THRESHOLD) {
                        findings.add(new Finding("HIGH", "BRUTE_FORCE_SUSPECTED", email,
                                countInWindow + " failed logins within 5 minutes from IP " + firstIp));
                        break;
                    }
                }
            }

            // Rule 2: Off-hours access - successful logins between 01:00-04:59
            for (LoginEvent s : successes) {
                int h = s.timestamp().getHour();
                if (h >= OFF_HOURS_START_HOUR && h <= OFF_HOURS_END_HOUR) {
                    findings.add(new Finding("MEDIUM", "OFF_HOURS_ACCESS", email,
                            "successful login at " + s.timestamp()));
                }
            }

            // Rule 3: Repeated failures - total failed >= 5
            if (failures.size() >= REPEATED_FAILURES_THRESHOLD) {
                findings.add(new Finding("HIGH", "REPEATED_FAILURES", email,
                        failures.size() + " failed logins in total"));
            }

            // Rule 4: Multiple IPs - 3+ distinct IPs for successful logins
            long distinctSuccessIps = successes.stream()
                    .map(LoginEvent::ip)
                    .distinct()
                    .count();
            if (distinctSuccessIps >= MULTI_IP_THRESHOLD) {
                findings.add(new Finding("LOW", "MULTIPLE_IPS", email,
                        distinctSuccessIps + " distinct IPs"));
            }
        }

        // Sort: HIGH > MEDIUM > LOW, then subject ascending
        return findings.stream()
                .sorted(Comparator
                        .comparing((Finding f) -> severityRank(f.severity()))
                        .thenComparing(Finding::subject))
                .toList();
    }

    private static int severityRank(String severity) {
        return switch (severity) { case "HIGH" -> 0; case "MEDIUM" -> 1; case "LOW" -> 2; default -> 3; };
    }
}