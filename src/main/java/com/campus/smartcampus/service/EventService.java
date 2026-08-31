package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.EventRequest;
import com.campus.smartcampus.dto.response.EventResponse;
import com.campus.smartcampus.entity.Event;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.EventCategory;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.EventRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public EventResponse createEvent(EventRequest request, UUID organizerId) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", organizerId));

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        Event event = Event.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .category(request.getCategory())
                .organizer(organizer)
                .venue(request.getVenue())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .maxAttendees(request.getMaxAttendees())
                .build();

        Event saved = eventRepository.save(event);
        log.info("Created event: {} ({})", saved.getTitle(), saved.getCategory());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public Page<EventResponse> getAllEvents(Pageable pageable) {
        return eventRepository.findAllByIsActiveTrue(pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<EventResponse> getUpcomingEvents(Pageable pageable) {
        return eventRepository.findAllByStartTimeAfterAndIsActiveTrueOrderByStartTimeAsc(Instant.now(), pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<EventResponse> getEventsByCategory(EventCategory category, Pageable pageable) {
        return eventRepository.findAllByCategoryAndIsActiveTrue(category, pageable).map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public EventResponse getEventById(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        return mapToResponse(event);
    }

    @Transactional
    public void rsvpEvent(UUID eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", eventId));

        if (!event.isRegistrationOpen()) {
            throw new BadRequestException("Registration is closed for this event");
        }

        if (event.getCurrentAttendees() >= event.getMaxAttendees()) {
            throw new BadRequestException("Event is at maximum attendee capacity");
        }

        event.setCurrentAttendees(event.getCurrentAttendees() + 1);
        if (event.getCurrentAttendees() >= event.getMaxAttendees()) {
            event.setRegistrationOpen(false);
        }
        eventRepository.save(event);
        log.info("RSVP recorded for event {}", event.getTitle());
    }

    @Transactional
    public void cancelEvent(UUID id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        event.setActive(false);
        event.setRegistrationOpen(false);
        eventRepository.save(event);
        log.info("Cancelled event: {}", event.getTitle());
    }

    private EventResponse mapToResponse(Event event) {
        return EventResponse.builder()
                .id(event.getId())
                .title(event.getTitle())
                .description(event.getDescription())
                .category(event.getCategory())
                .organizerId(event.getOrganizer().getId())
                .organizerName(event.getOrganizer().getFirstName() + " " + event.getOrganizer().getLastName())
                .venue(event.getVenue())
                .startTime(event.getStartTime())
                .endTime(event.getEndTime())
                .maxAttendees(event.getMaxAttendees())
                .currentAttendees(event.getCurrentAttendees())
                .isRegistrationOpen(event.isRegistrationOpen())
                .isActive(event.isActive())
                .build();
    }
}
