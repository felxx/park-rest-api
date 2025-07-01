package com.felxx.park_rest_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.felxx.park_rest_api.entities.Client;
import com.felxx.park_rest_api.web.dto.ClientCreateDto;
import com.felxx.park_rest_api.web.dto.ClientResponseDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientMapper {
    
    public static Client toClient(ClientCreateDto clientCreateDto) {
        return new ModelMapper().map(clientCreateDto, Client.class);
    }

    public static ClientResponseDto toDto(Client client) {
        return new ModelMapper().map(client, ClientResponseDto.class);
    }
}
