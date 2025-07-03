package com.felxx.park_rest_api.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.felxx.park_rest_api.entities.Client;
import com.felxx.park_rest_api.entities.ClientParkingSpace;
import com.felxx.park_rest_api.entities.ParkingSpace;
import com.felxx.park_rest_api.util.ParkingLotUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ParkingLotService {
    
    private final ClientParkingSpaceService clientParkingSpaceService;
    private final ClientService clientService;
    private final ParkingSpaceService parkingSpaceService;

    @Transactional
    public ClientParkingSpace checkIn(ClientParkingSpace clientParkingSpace) {
        Client client = clientService.findByCpf(clientParkingSpace.getClient().getCpf());
        clientParkingSpace.setClient(client);

        ParkingSpace parkingSpace = parkingSpaceService.findByAvailableParkingSpace();
        parkingSpace.setStatus(ParkingSpace.ParkingSpaceStatus.OCCUPIED);
        clientParkingSpace.setParkingSpace(parkingSpace);

        clientParkingSpace.setEntryDate(LocalDateTime.now());
        clientParkingSpace.setReceipt(ParkingLotUtils.generateReceipt());
        return clientParkingSpaceService.save(clientParkingSpace);

    }
}
