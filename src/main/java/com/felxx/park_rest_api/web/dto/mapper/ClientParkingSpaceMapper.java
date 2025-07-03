package com.felxx.park_rest_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.felxx.park_rest_api.entities.ClientParkingSpace;
import com.felxx.park_rest_api.web.dto.ParkingLotCreateDto;
import com.felxx.park_rest_api.web.dto.ParkingLotResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientParkingSpaceMapper {
    
    public static ClientParkingSpace toClientParkingSpace(ParkingLotCreateDto parkingLotCreateDto) {
        return new ModelMapper().map(parkingLotCreateDto, ClientParkingSpace.class);
    }

    public static ParkingLotResponseDto toDto(ClientParkingSpace clientParkingSpace) {
        return new ModelMapper().map(clientParkingSpace, ParkingLotResponseDto.class);
    }
}
