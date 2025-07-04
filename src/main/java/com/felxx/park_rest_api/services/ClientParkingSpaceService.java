package com.felxx.park_rest_api.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felxx.park_rest_api.entities.ClientParkingSpace;
import com.felxx.park_rest_api.repositories.ClientParkingSpaceRepository;
import com.felxx.park_rest_api.repositories.projection.ClientParkingSpaceProjection;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientParkingSpaceService {

    private final ClientParkingSpaceRepository clientParkingSpaceRepository;

    @Transactional
    public ClientParkingSpace save(ClientParkingSpace clientParkingSpace) {
        return clientParkingSpaceRepository.save(clientParkingSpace);
    }

    @Transactional(readOnly = true)
    public ClientParkingSpace findByReceipt(String receipt) {
        return clientParkingSpaceRepository.findByReceiptAndExitDateIsNull(receipt)
                .orElseThrow(() -> new IllegalArgumentException("Parking space not found for receipt: " + receipt));
    }

    @Transactional(readOnly = true)
    public long getNumberOfTimesParkingLotCompleted(String cpf) {
        return clientParkingSpaceRepository.countByClientCpfAndExitDateIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClientParkingSpaceProjection> findAllByCpf(String cpf, Pageable pageable) {
        return clientParkingSpaceRepository.findAllByClientCpf(cpf, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClientParkingSpaceProjection> findAllById(Long id, Pageable pageable) {
        return clientParkingSpaceRepository.findAllByClientUserId(id, pageable);
    }
}
