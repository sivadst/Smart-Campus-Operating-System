package com.campus.smartcampus.event;

import com.campus.smartcampus.enums.NotificationType;
import com.campus.smartcampus.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CampusEventListener {

    private final NotificationService notificationService;

    @Async("taskExecutor")
    @EventListener
    public void handleUserRegistered(UserRegisteredEvent event) {
        log.info("Async processing registration for user: {} ({})", event.getFullName(), event.getEmail());
        notificationService.createNotification(
                event.getUserId(),
                "Welcome to Smart Campus!",
                "Hello " + event.getFullName() + ", your account has been successfully created.",
                NotificationType.SYSTEM,
                "/api/v1/users/me"
        );
    }

    @Async("taskExecutor")
    @EventListener
    public void handleRoomBooking(RoomBookingEvent event) {
        log.info("Async processing booking status change: {} -> {}", event.getBookingId(), event.getStatus());
        notificationService.createNotification(
                event.getUserId(),
                "Room Booking Update",
                "Your booking status has been updated to: " + event.getStatus(),
                NotificationType.BOOKING_UPDATE,
                "/api/v1/bookings/" + event.getBookingId()
        );
    }
}
