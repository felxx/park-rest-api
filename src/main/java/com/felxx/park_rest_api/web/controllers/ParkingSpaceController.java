package com.felxx.park_rest_api.web.controllers;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.felxx.park_rest_api.entities.ParkingSpace;
import com.felxx.park_rest_api.services.ParkingSpaceService;
import com.felxx.park_rest_api.web.dto.ParkingSpaceCreateDto;
import com.felxx.park_rest_api.web.dto.ParkingSpaceResponseDto;
import com.felxx.park_rest_api.web.dto.UserResponseDto;
import com.felxx.park_rest_api.web.dto.mapper.ParkingSpaceMapper;
import com.felxx.park_rest_api.web.exceptions.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/parking-spaces")
public class ParkingSpaceController {
    
    private final ParkingSpaceService parkingSpaceService;


    @Operation(summary = "Create a new parking space", security = @SecurityRequirement(name = "security"), responses = {
        @ApiResponse(responseCode = "201", headers = @Header(name = HttpHeaders.LOCATION), description = "Parking space created successfully", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = UserResponseDto.class))),
        @ApiResponse(responseCode = "409", description = "Parking space already exists", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
        @ApiResponse(responseCode = "422", description = "Invalid input data", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid ParkingSpaceCreateDto parkingSpaceCreateDto) {
        ParkingSpace parkingSpace = ParkingSpaceMapper.toParkingSpace(parkingSpaceCreateDto);
        parkingSpaceService.save(parkingSpace);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{code}").buildAndExpand(parkingSpace.getCode()).toUri();
        return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Find parking space by code", security = @SecurityRequirement(name = "security"), responses = {
        @ApiResponse(responseCode = "200", description = "Parking space found", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ParkingSpaceResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Parking space not found", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{code}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ParkingSpaceResponseDto> findByCode(@PathVariable String code) {
        ParkingSpace parkingSpace = parkingSpaceService.findByCode(code);
        return ResponseEntity.ok(ParkingSpaceMapper.toDto(parkingSpace));
    }
}
