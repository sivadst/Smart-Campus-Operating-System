package com.campus.smartcampus.entity;

import com.campus.smartcampus.audit.Auditable;
import com.campus.smartcampus.enums.RoomType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_number", nullable = false)
    private String roomNumber;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type", nullable = false)
    private RoomType roomType;

    @Column(nullable = false)
    @Builder.Default
    private int capacity = 30;

    @Column(nullable = false)
    private int floor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id", nullable = false)
    private Building building;

    @Column(name = "has_projector")
    @Builder.Default
    private boolean hasProjector = false;

    @Column(name = "has_ac")
    @Builder.Default
    private boolean hasAC = false;

    @Column(name = "has_wifi")
    @Builder.Default
    private boolean hasWifi = true;

    @Column(name = "is_available", nullable = false)
    @Builder.Default
    private boolean isAvailable = true;
}
