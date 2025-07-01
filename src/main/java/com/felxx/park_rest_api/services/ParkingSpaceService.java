package com.felxx.park_rest_api.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.felxx.park_rest_api.entities.ParkingSpace;
import com.felxx.park_rest_api.exceptions.EntityNotFoundException;
import com.felxx.park_rest_api.exceptions.UniqueCodeViolationException;
import com.felxx.park_rest_api.repositories.ParkingSpaceRepository;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {
    
    private final ParkingSpaceRepository parkingSpaceRepository;

    @Transactional
    public ParkingSpace save(ParkingSpace parkingSpace){
        try{
            return parkingSpaceRepository.save(parkingSpace);
        } catch (DataIntegrityViolationException e) {
            throw new UniqueCodeViolationException("A parking space with the code '" + parkingSpace.getCode() + "' already exists.");
        }
    }

    @Transactional(readOnly = true)
    public ParkingSpace findByCode(String code) {
        return parkingSpaceRepository.findByCode(code).orElseThrow(() -> new EntityNotFoundException("Parking space not found with code: " + code));
    }
}
