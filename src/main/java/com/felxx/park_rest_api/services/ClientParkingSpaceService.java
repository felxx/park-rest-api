package com.felxx.park_rest_api.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felxx.park_rest_api.entities.ClientParkingSpace;
import com.felxx.park_rest_api.repositories.ClientParkingSpaceRepository;

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
}
