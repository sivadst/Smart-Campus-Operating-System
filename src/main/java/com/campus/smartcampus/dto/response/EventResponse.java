package com.campus.smartcampus.dto.response;

import com.campus.smartcampus.enums.EventCategory;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventResponse {
    private UUID id;
    private String title;
    private String description;
    private EventCategory category;
    private UUID organizerId;
    private String organizerName;
    private String venue;
    private Instant startTime;
    private Instant endTime;
    private int maxAttendees;
    private int currentAttendees;
    private boolean isRegistrationOpen;
    private boolean isActive;
}
