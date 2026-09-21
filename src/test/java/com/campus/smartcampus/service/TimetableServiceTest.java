package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.TimetableRequest;
import com.campus.smartcampus.dto.response.TimetableResponse;
import com.campus.smartcampus.entity.Building;
import com.campus.smartcampus.entity.Course;
import com.campus.smartcampus.entity.Room;
import com.campus.smartcampus.entity.TimetableEntry;
import com.campus.smartcampus.entity.User;
import com.campus.smartcampus.enums.DayOfWeekEnum;
import com.campus.smartcampus.enums.RoomType;
import com.campus.smartcampus.exception.BadRequestException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.CourseRepository;
import com.campus.smartcampus.repository.RoomRepository;
import com.campus.smartcampus.repository.TimetableEntryRepository;
import com.campus.smartcampus.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TimetableService Unit Tests")
class TimetableServiceTest {

    @Mock
    private TimetableEntryRepository timetableRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TimetableService timetableService;

    private Course course;
    private Room room;
    private User faculty;
    private TimetableEntry entry;
    private TimetableRequest request;
    private UUID courseId;
    private UUID roomId;
    private UUID facultyId;
    private UUID entryId;

    @BeforeEach
    void setUp() {
        courseId = UUID.randomUUID();
        roomId = UUID.randomUUID();
        facultyId = UUID.randomUUID();
        entryId = UUID.randomUUID();

        Building building = Building.builder()
                .id(UUID.randomUUID())
                .name("Engineering Block A")
                .code("ENG-A")
                .build();

        course = Course.builder()
                .id(courseId)
                .code("CS301")
                .name("Database Systems")
                .build();

        room = Room.builder()
                .id(roomId)
                .roomNumber("101")
                .building(building)
                .roomType(RoomType.LECTURE_HALL)
                .build();

        faculty = User.builder()
                .id(facultyId)
                .firstName("Robert")
                .lastName("Taylor")
                .email("robert.taylor@campus.edu")
                .build();

        entry = TimetableEntry.builder()
                .id(entryId)
                .course(course)
                .room(room)
                .faculty(faculty)
                .dayOfWeek(DayOfWeekEnum.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 30))
                .academicYear("2026-2027")
                .semester(5)
                .section("A")
                .build();

        request = TimetableRequest.builder()
                .courseId(courseId)
                .roomId(roomId)
                .facultyId(facultyId)
                .dayOfWeek(DayOfWeekEnum.MONDAY)
                .startTime(LocalTime.of(9, 0))
                .endTime(LocalTime.of(10, 30))
                .academicYear("2026-2027")
                .semester(5)
                .section("A")
                .build();
    }

    @Test
    @DisplayName("createEntry should successfully persist and return timetable response")
    void createEntry_Success() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(facultyId)).thenReturn(Optional.of(faculty));
        when(timetableRepository.save(any(TimetableEntry.class))).thenReturn(entry);

        TimetableResponse response = timetableService.createEntry(request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(entryId);
        assertThat(response.getCourseCode()).isEqualTo("CS301");
        assertThat(response.getRoomNumber()).isEqualTo("101");
        assertThat(response.getFacultyName()).isEqualTo("Robert Taylor");
        assertThat(response.getDayOfWeek()).isEqualTo(DayOfWeekEnum.MONDAY);
        verify(timetableRepository, times(1)).save(any(TimetableEntry.class));
    }

    @Test
    @DisplayName("createEntry should throw BadRequestException when start time is after end time")
    void createEntry_ThrowsBadRequest_WhenStartTimeAfterEndTime() {
        request.setStartTime(LocalTime.of(11, 0));
        request.setEndTime(LocalTime.of(9, 0));

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(userRepository.findById(facultyId)).thenReturn(Optional.of(faculty));

        assertThatThrownBy(() -> timetableService.createEntry(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Start time must be before end time");

        verify(timetableRepository, never()).save(any());
    }

    @Test
    @DisplayName("createEntry should throw ResourceNotFoundException when course is not found")
    void createEntry_ThrowsNotFound_WhenCourseNotFound() {
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> timetableService.createEntry(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Course");

        verify(timetableRepository, never()).save(any());
    }

    @Test
    @DisplayName("getFacultySchedule should return entries for specific day")
    void getFacultySchedule_ReturnsList() {
        when(timetableRepository.findAllByFacultyIdAndDayOfWeek(facultyId, DayOfWeekEnum.MONDAY))
                .thenReturn(List.of(entry));

        List<TimetableResponse> results = timetableService.getFacultySchedule(facultyId, DayOfWeekEnum.MONDAY);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCourseCode()).isEqualTo("CS301");
        verify(timetableRepository, times(1)).findAllByFacultyIdAndDayOfWeek(facultyId, DayOfWeekEnum.MONDAY);
    }

    @Test
    @DisplayName("getCourseSchedule should return all entries for course")
    void getCourseSchedule_ReturnsList() {
        when(timetableRepository.findAllByCourseId(courseId)).thenReturn(List.of(entry));

        List<TimetableResponse> results = timetableService.getCourseSchedule(courseId);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getRoomNumber()).isEqualTo("101");
        verify(timetableRepository, times(1)).findAllByCourseId(courseId);
    }

    @Test
    @DisplayName("getRoomSchedule should return all entries for room on given day")
    void getRoomSchedule_ReturnsList() {
        when(timetableRepository.findAllByRoomIdAndDayOfWeek(roomId, DayOfWeekEnum.MONDAY))
                .thenReturn(List.of(entry));

        List<TimetableResponse> results = timetableService.getRoomSchedule(roomId, DayOfWeekEnum.MONDAY);

        assertThat(results).hasSize(1);
        assertThat(results.get(0).getCourseCode()).isEqualTo("CS301");
        verify(timetableRepository, times(1)).findAllByRoomIdAndDayOfWeek(roomId, DayOfWeekEnum.MONDAY);
    }

    @Test
    @DisplayName("deleteEntry should delete entry when it exists")
    void deleteEntry_Success() {
        when(timetableRepository.existsById(entryId)).thenReturn(true);
        doNothing().when(timetableRepository).deleteById(entryId);

        timetableService.deleteEntry(entryId);

        verify(timetableRepository, times(1)).deleteById(entryId);
    }

    @Test
    @DisplayName("deleteEntry should throw ResourceNotFoundException when entry does not exist")
    void deleteEntry_ThrowsNotFound_WhenDoesNotExist() {
        when(timetableRepository.existsById(entryId)).thenReturn(false);

        assertThatThrownBy(() -> timetableService.deleteEntry(entryId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("TimetableEntry");

        verify(timetableRepository, never()).deleteById(any());
    }
}
