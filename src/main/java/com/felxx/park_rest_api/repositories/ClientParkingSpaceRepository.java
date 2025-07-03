package com.felxx.park_rest_api.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.felxx.park_rest_api.entities.ClientParkingSpace;

public interface ClientParkingSpaceRepository extends JpaRepository<ClientParkingSpace, Long> {

    Optional<ClientParkingSpace> findByReceiptAndExitDateIsNull(String receipt);

    long countByClientCpfAndExitDateIsNotNull(String cpf);
}
