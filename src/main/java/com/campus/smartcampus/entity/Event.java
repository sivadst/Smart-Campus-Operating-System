package com.campus.smartcampus.entity;

import com.campus.smartcampus.audit.Auditable;
import com.campus.smartcampus.enums.EventCategory;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event extends Auditable {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id", nullable = false)
    private User organizer;

    private String venue;

    @Column(nullable = false)
    private Instant startTime;

    @Column(nullable = false)
    private Instant endTime;

    private int maxAttendees;

    @Builder.Default
    private int currentAttendees = 0;

    @Builder.Default
    @Column(nullable = false)
    private boolean isRegistrationOpen = true;

    @Builder.Default
    @Column(nullable = false)
    private boolean isActive = true;
}
