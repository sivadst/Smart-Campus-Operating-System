package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.RoomRequest;
import com.campus.smartcampus.dto.response.RoomResponse;
import com.campus.smartcampus.entity.Building;
import com.campus.smartcampus.entity.Room;
import com.campus.smartcampus.enums.RoomType;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.BuildingRepository;
import com.campus.smartcampus.repository.RoomRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("RoomService Unit Tests")
class RoomServiceTest {

    @Mock private RoomRepository roomRepository;
    @Mock private BuildingRepository buildingRepository;
    @InjectMocks private RoomService roomService;

    private Building building;
    private Room room;
    private UUID roomId, buildingId;

    @BeforeEach
    void setUp() {
        buildingId = UUID.randomUUID();
        roomId = UUID.randomUUID();

        building = Building.builder().id(buildingId).name("Science Complex").code("SC").build();

        room = Room.builder()
                .id(roomId)
                .roomNumber("SC-101")
                .name("Computer Lab 1")
                .roomType(RoomType.COMPUTER_LAB)
                .capacity(40)
                .floor(1)
                .building(building)
                .hasProjector(true)
                .hasAC(true)
                .hasWifi(true)
                .isAvailable(true)
                .build();
    }

    @Test
    @DisplayName("Should create room successfully")
    void createRoom_ValidRequest_ReturnsRoomResponse() {
        RoomRequest request = RoomRequest.builder()
                .roomNumber("SC-101").name("Computer Lab 1")
                .roomType(RoomType.COMPUTER_LAB).capacity(40).floor(1)
                .buildingId(buildingId).hasProjector(true).hasAC(true).hasWifi(true)
                .build();

        when(buildingRepository.findById(buildingId)).thenReturn(Optional.of(building));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        RoomResponse response = roomService.createRoom(request);

        assertThat(response).isNotNull();
        assertThat(response.getRoomNumber()).isEqualTo("SC-101");
        assertThat(response.getBuildingName()).isEqualTo("Science Complex");
    }

    @Test
    @DisplayName("Should toggle room availability")
    void toggleAvailability_ExistingRoom_InvertsState() {
        when(roomRepository.findById(roomId)).thenReturn(Optional.of(room));
        when(roomRepository.save(any(Room.class))).thenReturn(room);

        roomService.toggleAvailability(roomId);

        verify(roomRepository).save(argThat(r -> !r.isAvailable()));
    }
}
