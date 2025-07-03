package com.felxx.park_rest_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felxx.park_rest_api.entities.ParkingSpace;
import com.felxx.park_rest_api.entities.ParkingSpace.ParkingSpaceStatus;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {

    Optional<ParkingSpace> findByCode(String code);

    Optional<ParkingSpace> findFirstByStatus(ParkingSpaceStatus status);
}
