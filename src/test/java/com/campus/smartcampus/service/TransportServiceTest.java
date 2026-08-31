package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.BusRouteRequest;
import com.campus.smartcampus.dto.response.BusRouteResponse;
import com.campus.smartcampus.entity.BusRoute;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.repository.BusRouteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TransportService Unit Tests")
class TransportServiceTest {

    @Mock private BusRouteRepository busRouteRepository;
    @InjectMocks private TransportService transportService;

    private BusRoute route;

    @BeforeEach
    void setUp() {
        route = BusRoute.builder()
                .id(UUID.randomUUID()).routeNumber("R-101").source("Metro Station")
                .destination("Main Campus Gate").stops("Station -> Gate A -> Gate B")
                .totalSeats(50).isActive(true)
                .build();
    }

    @Test
    @DisplayName("Should create bus route successfully")
    void createRoute_ValidRequest_ReturnsBusRouteResponse() {
        BusRouteRequest request = BusRouteRequest.builder()
                .routeNumber("R-101").source("Metro Station").destination("Main Campus Gate").totalSeats(50)
                .build();

        when(busRouteRepository.existsByRouteNumber("R-101")).thenReturn(false);
        when(busRouteRepository.save(any(BusRoute.class))).thenReturn(route);

        BusRouteResponse response = transportService.createRoute(request);

        assertThat(response).isNotNull();
        assertThat(response.getRouteNumber()).isEqualTo("R-101");
        assertThat(response.getTotalSeats()).isEqualTo(50);
    }

    @Test
    @DisplayName("Should throw DuplicateResourceException on duplicate route number")
    void createRoute_DuplicateNumber_ThrowsException() {
        BusRouteRequest request = BusRouteRequest.builder().routeNumber("R-101").build();
        when(busRouteRepository.existsByRouteNumber("R-101")).thenReturn(true);

        assertThatThrownBy(() -> transportService.createRoute(request))
                .isInstanceOf(DuplicateResourceException.class);
    }
}
