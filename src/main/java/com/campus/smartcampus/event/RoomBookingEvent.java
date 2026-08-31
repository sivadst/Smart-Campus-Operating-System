package com.campus.smartcampus.event;

import com.campus.smartcampus.enums.BookingStatus;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.UUID;

@Getter
public class RoomBookingEvent extends ApplicationEvent {
    private final UUID bookingId;
    private final UUID roomId;
    private final UUID userId;
    private final BookingStatus status;

    public RoomBookingEvent(Object source, UUID bookingId, UUID roomId, UUID userId, BookingStatus status) {
        super(source);
        this.bookingId = bookingId;
        this.roomId = roomId;
        this.userId = userId;
        this.status = status;
    }
}
