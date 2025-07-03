package com.felxx.park_rest_api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felxx.park_rest_api.entities.ClientParkingSpace;

public interface ClientParkingSpaceRepository extends JpaRepository<ClientParkingSpace, Long> {
}
