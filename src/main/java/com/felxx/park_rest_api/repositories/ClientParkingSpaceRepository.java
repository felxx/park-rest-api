package com.felxx.park_rest_api.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.felxx.park_rest_api.entities.ClientParkingSpace;
import com.felxx.park_rest_api.repositories.projection.ClientParkingSpaceProjection;

public interface ClientParkingSpaceRepository extends JpaRepository<ClientParkingSpace, Long> {

    Optional<ClientParkingSpace> findByReceiptAndExitDateIsNull(String receipt);

    long countByClientCpfAndExitDateIsNotNull(String cpf);

    Page<ClientParkingSpaceProjection> findAllByClientCpf(String cpf, Pageable pageable);

    Page<ClientParkingSpaceProjection> findAllByClientUserId(Long id, Pageable pageable);
}
