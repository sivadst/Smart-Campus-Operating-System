package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.BuildingRequest;
import com.campus.smartcampus.dto.response.BuildingResponse;
import com.campus.smartcampus.entity.Building;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.BuildingRepository;
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
public class BuildingService {

    private final BuildingRepository buildingRepository;

    @Transactional
    public BuildingResponse createBuilding(BuildingRequest request) {
        if (buildingRepository.existsByCode(request.getCode())) {
            throw new DuplicateResourceException("Building", "code", request.getCode());
        }

        Building building = Building.builder()
                .name(request.getName())
                .code(request.getCode())
                .address(request.getAddress())
                .totalFloors(request.getTotalFloors())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .build();

        Building saved = buildingRepository.save(building);
        log.info("Created building: {} ({})", saved.getName(), saved.getCode());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BuildingResponse> getAllBuildings() {
        return buildingRepository.findAllByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BuildingResponse getBuildingById(UUID id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", id));
        return mapToResponse(building);
    }

    @Transactional
    public BuildingResponse updateBuilding(UUID id, BuildingRequest request) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", id));

        building.setName(request.getName());
        building.setAddress(request.getAddress());
        building.setTotalFloors(request.getTotalFloors());
        building.setLatitude(request.getLatitude());
        building.setLongitude(request.getLongitude());

        Building saved = buildingRepository.save(building);
        log.info("Updated building: {}", saved.getCode());
        return mapToResponse(saved);
    }

    @Transactional
    public void deleteBuilding(UUID id) {
        Building building = buildingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Building", "id", id));
        building.setActive(false);
        buildingRepository.save(building);
        log.info("Soft-deleted building: {}", building.getCode());
    }

    private BuildingResponse mapToResponse(Building building) {
        return BuildingResponse.builder()
                .id(building.getId())
                .name(building.getName())
                .code(building.getCode())
                .address(building.getAddress())
                .totalFloors(building.getTotalFloors())
                .latitude(building.getLatitude())
                .longitude(building.getLongitude())
                .isActive(building.isActive())
                .roomCount(building.getRooms() != null ? building.getRooms().size() : 0)
                .build();
    }
}
