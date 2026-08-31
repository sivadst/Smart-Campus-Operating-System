package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.BusRoute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BusRouteRepository extends JpaRepository<BusRoute, UUID> {
    Optional<BusRoute> findByRouteNumber(String routeNumber);
    boolean existsByRouteNumber(String routeNumber);
    List<BusRoute> findAllByIsActiveTrue();
}
