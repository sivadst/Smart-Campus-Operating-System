package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Room;
import com.campus.smartcampus.enums.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findAllByBuildingId(UUID buildingId);
    List<Room> findAllByRoomType(RoomType roomType);
    List<Room> findAllByIsAvailableTrue();
    List<Room> findAllByBuildingIdAndFloor(UUID buildingId, int floor);
    List<Room> findAllByCapacityGreaterThanEqual(int capacity);
}
