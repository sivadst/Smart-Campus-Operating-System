package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.RoomRequest;
import com.campus.smartcampus.dto.response.RoomResponse;
import com.campus.smartcampus.entity.Building;
import com.campus.smartcampus.entity.Room;
import com.campus.smartcampus.enums.RoomType;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.BuildingRepository;
import com.campus.smartcampus.repository.RoomRepository;
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
public class RoomService {

    private final RoomRepository roomRepository;
    private final BuildingRepository buildingRepository;

    @Transactional
    public RoomResponse createRoom(RoomRequest request) {
        Building building = buildingRepository.findById(request.getBuildingId())
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", request.getBuildingId()));

        Room room = Room.builder()
                .roomNumber(request.getRoomNumber())
                .name(request.getName())
                .roomType(request.getRoomType())
                .capacity(request.getCapacity())
                .floor(request.getFloor())
                .building(building)
                .hasProjector(request.isHasProjector())
                .hasAC(request.isHasAC())
                .hasWifi(request.isHasWifi())
                .build();

        Room saved = roomRepository.save(room);
        log.info("Created room: {} in building {}", saved.getRoomNumber(), building.getCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByBuilding(UUID buildingId) {
        return roomRepository.findAllByBuildingId(buildingId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getAvailableRooms() {
        return roomRepository.findAllByIsAvailableTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByType(RoomType roomType) {
        return roomRepository.findAllByRoomType(roomType).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RoomResponse> getRoomsByCapacity(int minCapacity) {
        return roomRepository.findAllByCapacityGreaterThanEqual(minCapacity).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RoomResponse getRoomById(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        return mapToResponse(room);
    }

    @Transactional
    public RoomResponse updateRoom(UUID id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));

        room.setName(request.getName());
        room.setRoomType(request.getRoomType());
        room.setCapacity(request.getCapacity());
        room.setHasProjector(request.isHasProjector());
        room.setHasAC(request.isHasAC());
        room.setHasWifi(request.isHasWifi());

        Room saved = roomRepository.save(room);
        log.info("Updated room: {}", saved.getRoomNumber());
        return mapToResponse(saved);
    }

    @Transactional
    public void toggleAvailability(UUID id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room", "id", id));
        room.setAvailable(!room.isAvailable());
        roomRepository.save(room);
        log.info("Toggled room {} availability to {}", room.getRoomNumber(), room.isAvailable());
    }

    private RoomResponse mapToResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .roomNumber(room.getRoomNumber())
                .name(room.getName())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .floor(room.getFloor())
                .buildingId(room.getBuilding().getId())
                .buildingName(room.getBuilding().getName())
                .hasProjector(room.isHasProjector())
                .hasAC(room.isHasAC())
                .hasWifi(room.isHasWifi())
                .isAvailable(room.isAvailable())
                .build();
    }
}
