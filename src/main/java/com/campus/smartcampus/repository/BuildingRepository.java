package com.campus.smartcampus.repository;

import com.campus.smartcampus.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BuildingRepository extends JpaRepository<Building, UUID> {
    Optional<Building> findByCode(String code);
    boolean existsByCode(String code);
    List<Building> findAllByIsActiveTrue();
}
