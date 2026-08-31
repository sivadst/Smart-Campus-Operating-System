package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.BuildingRequest;
import com.campus.smartcampus.dto.response.BuildingResponse;
import com.campus.smartcampus.entity.Building;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.BuildingRepository;
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
@DisplayName("BuildingService Unit Tests")
class BuildingServiceTest {

    @Mock private BuildingRepository buildingRepository;
    @InjectMocks private BuildingService buildingService;

    private Building building;
    private UUID buildingId;

    @BeforeEach
    void setUp() {
        buildingId = UUID.randomUUID();
        building = Building.builder()
                .id(buildingId)
                .name("Alan Turing Hall")
                .code("ATH")
                .address("North Campus")
                .totalFloors(4)
                .isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should create building successfully")
    void createBuilding_ValidRequest_ReturnsBuildingResponse() {
        BuildingRequest request = BuildingRequest.builder()
                .name("Alan Turing Hall").code("ATH").address("North Campus").totalFloors(4).build();

        when(buildingRepository.existsByCode("ATH")).thenReturn(false);
        when(buildingRepository.save(any(Building.class))).thenReturn(building);

        BuildingResponse response = buildingService.createBuilding(request);

        assertThat(response).isNotNull();
        assertThat(response.getCode()).isEqualTo("ATH");
        assertThat(response.getTotalFloors()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should throw when building code exists")
    void createBuilding_DuplicateCode_ThrowsException() {
        BuildingRequest request = BuildingRequest.builder()
                .name("Alan Turing Hall").code("ATH").build();

        when(buildingRepository.existsByCode("ATH")).thenReturn(true);

        assertThatThrownBy(() -> buildingService.createBuilding(request))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    @DisplayName("Should return all active buildings")
    void getAllBuildings_ReturnsActiveList() {
        when(buildingRepository.findAllByIsActiveTrue()).thenReturn(List.of(building));

        List<BuildingResponse> result = buildingService.getAllBuildings();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Alan Turing Hall");
    }

    @Test
    @DisplayName("Should soft-delete building")
    void deleteBuilding_ExistingId_SetsInactive() {
        when(buildingRepository.findById(buildingId)).thenReturn(Optional.of(building));
        when(buildingRepository.save(any(Building.class))).thenReturn(building);

        buildingService.deleteBuilding(buildingId);

        verify(buildingRepository).save(argThat(b -> !b.isActive()));
    }
}
