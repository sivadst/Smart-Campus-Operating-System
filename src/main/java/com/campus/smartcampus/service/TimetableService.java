package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.TimetableRequest;
import com.campus.smartcampus.dto.response.TimetableResponse;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.Room;
import com.campus.smartcampus.entity.TimetableEntry;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.DayOfWeekEnum;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.RoomRepository;
import com.campus.smartcampus.repository.TimetableEntryRepository;
import com.campus.smartcampus.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TimetableService {

    private final TimetableEntryRepository timetableRepository;
    private final CourseRepository courseRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;

    @Transactional
    public TimetableResponse createEntry(TimetableRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course", "id", request.getCourseId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", request.getRoomId()));
        User faculty = userRepository.findById(request.getFacultyId())
                .orElseThrow(() -> new ResourceNotFoundException("Faculty", "id", request.getFacultyId()));

        if (request.getStartTime().isAfter(request.getEndTime())) {
            throw new BadRequestException("Start time must be before end time");
        }

        TimetableEntry entry = TimetableEntry.builder()
                .course(course)
                .room(room)
                .faculty(faculty)
                .dayOfWeek(request.getDayOfWeek())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .academicYear(request.getAcademicYear())
                .semester(request.getSemester())
                .section(request.getSection())
                .build();

        TimetableEntry saved = timetableRepository.save(entry);
        log.info("Created timetable entry for {} on {} at {}",
                course.getCode(), request.getDayOfWeek(), request.getStartTime());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getFacultySchedule(UUID facultyId, DayOfWeekEnum day) {
        return timetableRepository.findAllByFacultyIdAndDayOfWeek(facultyId, day).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getCourseSchedule(UUID courseId) {
        return timetableRepository.findAllByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TimetableResponse> getRoomSchedule(UUID roomId, DayOfWeekEnum day) {
        return timetableRepository.findAllByRoomIdAndDayOfWeek(roomId, day).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteEntry(UUID id) {
        if (!timetableRepository.existsById(id)) {
            throw new ResourceNotFoundException("TimetableEntry", "id", id);
        }
        timetableRepository.deleteById(id);
        log.info("Deleted timetable entry {}", id);
    }

    private TimetableResponse mapToResponse(TimetableEntry entry) {
        return TimetableResponse.builder()
                .id(entry.getId())
                .courseId(entry.getCourse().getId())
                .courseCode(entry.getCourse().getCode())
                .courseName(entry.getCourse().getName())
                .roomId(entry.getRoom().getId())
                .roomNumber(entry.getRoom().getRoomNumber())
                .buildingName(entry.getRoom().getBuilding().getName())
                .facultyId(entry.getFaculty().getId())
                .facultyName(entry.getFaculty().getFirstName() + " " + entry.getFaculty().getLastName())
                .dayOfWeek(entry.getDayOfWeek())
                .startTime(entry.getStartTime())
                .endTime(entry.getEndTime())
                .academicYear(entry.getAcademicYear())
                .semester(entry.getSemester())
                .section(entry.getSection())
                .build();
    }
}
