package com.campus.smartcampus.service;

import com.campus.smartcampus.dto.request.BusRouteRequest;
import com.campus.smartcampus.dto.response.BusRouteResponse;
import com.campus.smartcampus.entity.BusRoute;
import com.campus.smartcampus.exception.DuplicateResourceException;
import com.campus.smartcampus.exception.ResourceNotFoundException;
import com.campus.smartcampus.repository.BusRouteRepository;
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
public class TransportService {

    private final BusRouteRepository busRouteRepository;

    @Transactional
    public BusRouteResponse createRoute(BusRouteRequest request) {
        if (busRouteRepository.existsByRouteNumber(request.getRouteNumber())) {
            throw new DuplicateResourceException("BusRoute", "routeNumber", request.getRouteNumber());
        }

        BusRoute route = BusRoute.builder()
                .routeNumber(request.getRouteNumber())
                .source(request.getSource())
                .destination(request.getDestination())
                .stops(request.getStops())
                .totalSeats(request.getTotalSeats())
                .driverName(request.getDriverName())
                .driverPhone(request.getDriverPhone())
                .build();

        BusRoute saved = busRouteRepository.save(route);
        log.info("Created bus route: {}", saved.getRouteNumber());
        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<BusRouteResponse> getAllActiveRoutes() {
        return busRouteRepository.findAllByIsActiveTrue().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public BusRouteResponse getRouteById(UUID id) {
        BusRoute route = busRouteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusRoute", "id", id));
        return mapToResponse(route);
    }

    @Transactional
    public void deactivateRoute(UUID id) {
        BusRoute route = busRouteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BusRoute", "id", id));
        route.setActive(false);
        busRouteRepository.save(route);
        log.info("Deactivated bus route: {}", route.getRouteNumber());
    }

    private BusRouteResponse mapToResponse(BusRoute route) {
        return BusRouteResponse.builder()
                .id(route.getId())
                .routeNumber(route.getRouteNumber())
                .source(route.getSource())
                .destination(route.getDestination())
                .stops(route.getStops())
                .totalSeats(route.getTotalSeats())
                .driverName(route.getDriverName())
                .driverPhone(route.getDriverPhone())
                .isActive(route.isActive())
                .build();
    }
}
