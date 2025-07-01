package com.felxx.park_rest_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.felxx.park_rest_api.entities.ParkingSpace;
import com.felxx.park_rest_api.web.dto.ParkingSpaceCreateDto;

import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class ParkingSpaceMapper {
    
    public static ParkingSpace toParkingSpace(ParkingSpaceCreateDto dto) {
        return new ModelMapper().map(dto, ParkingSpace.class);
    }

    public static ParkingSpaceCreateDto toParkingSpaceCreateDto(ParkingSpace parkingSpace) {
        return new ModelMapper().map(parkingSpace, ParkingSpaceCreateDto.class);
    }
}
