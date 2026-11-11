package com.campus.smartcampus.service.ai;

import com.campus.smartcampus.service.ai.dataset.Records;
import com.campus.smartcampus.service.ai.dataset.Records.Notice;
import com.campus.smartcampus.service.ai.dataset.Records.NoticeCategory;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Rule-based ranking for notices. Not ML — deterministic additive scoring
 * with category weights, pin bonus, recency, audience match, and expiry penalty.
 */
public final class NoticePrioritizer {

    private NoticePrioritizer() {}

    public record ScoredNotice(Notice notice, double score, String reason) {}

    // Category weights
    private static final int EMERGENCY_WEIGHT = 5;
    private static final int EXAMINATION_WEIGHT = 4;
    private static final int ACADEMIC_WEIGHT = 3;
    private static final int PLACEMENT_WEIGHT = 2;
    private static final int EVENT_WEIGHT = 2;
    private static final int SPORTS_WEIGHT = 1;
    private static final int CULTURAL_WEIGHT = 1;
    private static final int ADMINISTRATIVE_WEIGHT = 2;
    private static final int GENERAL_WEIGHT = 1;

    // Scoring constants
    private static final int PIN_BONUS = 4;
    private static final int RECENCY_MAX = 3;
    private static final int AUDIENCE_BONUS = 2;
    private static final int EXPIRY_PENALTY = -1;

    /** Rank notices best-first. Expired, unpublished notices are dropped. */
    public static List<ScoredNotice> rank(List<Notice> notices,
                                          LocalDateTime now,
                                          String audienceRole,
                                          int limit) {
        List<ScoredNotice> scored = new ArrayList<>();

        for (Notice notice : notices) {
            if (!notice.published()) {
                continue;
            }
            if (notice.expiresAt() != null && now.isAfter(notice.expiresAt())) {
                continue;
            }

            int categoryWeight = categoryWeight(notice.category());
            int pinBonus = notice.pinned() ? PIN_BONUS : 0;
            int recencyScore = recencyScore(notice.publishedAt(), now);
            int audienceBonus = audienceBonus(notice.targetRole(), audienceRole);
            int expiryPenalty = expiryPenalty(notice.expiresAt(), now);

            double total = categoryWeight + pinBonus + recencyScore + audienceBonus + expiryPenalty;
            String reason = buildReason(notice, categoryWeight, pinBonus, recencyScore, audienceBonus, expiryPenalty);

            scored.add(new ScoredNotice(notice, total, reason));
        }

        scored.sort(Comparator
                .comparingDouble((ScoredNotice sn) -> -sn.score())
                .thenComparing(sn -> sn.notice().id()));

        if (limit > 0 && scored.size() > limit) {
            return scored.subList(0, limit);
        }
        return scored;
    }

    private static int categoryWeight(NoticeCategory category) {
        return switch (category) {
            case EMERGENCY -> EMERGENCY_WEIGHT;
            case EXAMINATION -> EXAMINATION_WEIGHT;
            case ACADEMIC -> ACADEMIC_WEIGHT;
            case PLACEMENT -> PLACEMENT_WEIGHT;
            case EVENT -> EVENT_WEIGHT;
            case SPORTS -> SPORTS_WEIGHT;
            case CULTURAL -> CULTURAL_WEIGHT;
            case ADMINISTRATIVE -> ADMINISTRATIVE_WEIGHT;
            case GENERAL -> GENERAL_WEIGHT;
        };
    }

    private static int recencyScore(LocalDateTime publishedAt, LocalDateTime now) {
        long daysBetween = ChronoUnit.DAYS.between(publishedAt.toLocalDate(), now.toLocalDate());
        int score = RECENCY_MAX - (int) daysBetween;
        return Math.max(0, score);
    }

    private static int audienceBonus(String targetRole, String audienceRole) {
        if (audienceRole == null) {
            return 0;
        }
        String target = targetRole != null ? targetRole : "";
        if (target.equalsIgnoreCase("ALL") || target.equalsIgnoreCase(audienceRole)) {
            return AUDIENCE_BONUS;
        }
        return 0;
    }

    private static int expiryPenalty(LocalDateTime expiresAt, LocalDateTime now) {
        if (expiresAt == null) {
            return 0;
        }
        long daysUntilExpiry = ChronoUnit.DAYS.between(now.toLocalDate(), expiresAt.toLocalDate());
        if (daysUntilExpiry >= 0 && daysUntilExpiry <= 3) {
            return EXPIRY_PENALTY;
        }
        return 0;
    }

    private static String buildReason(Notice notice, int categoryWeight, int pinBonus,
                                       int recencyScore, int audienceBonus, int expiryPenalty) {
        StringBuilder sb = new StringBuilder();
        sb.append(notice.category()).append(" (").append(categoryWeight).append(")");
        if (pinBonus > 0) sb.append(", pinned (+").append(pinBonus).append(")");
        if (recencyScore > 0) sb.append(", fresh (+").append(recencyScore).append(")");
        if (audienceBonus > 0) sb.append(", for ").append(notice.targetRole()).append(" (+").append(audienceBonus).append(")");
        if (expiryPenalty < 0) sb.append(", expiring soon (").append(expiryPenalty).append(")");
        return sb.toString();
    }
}