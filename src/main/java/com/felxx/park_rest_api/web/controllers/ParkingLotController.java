package com.felxx.park_rest_api.web.controllers;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.felxx.park_rest_api.entities.ClientParkingSpace;
import com.felxx.park_rest_api.services.ClientParkingSpaceService;
import com.felxx.park_rest_api.services.ParkingLotService;
import com.felxx.park_rest_api.web.dto.ParkingLotCreateDto;
import com.felxx.park_rest_api.web.dto.ParkingLotResponseDto;
import com.felxx.park_rest_api.web.dto.mapper.ClientParkingSpaceMapper;
import com.felxx.park_rest_api.web.exceptions.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/parking-lots")
public class ParkingLotController {

        private final ParkingLotService parkingLotService;
        private final ClientParkingSpaceService clientParkingSpaceService;

        @Operation(summary = "Check-in Operation", description = "Endpoint for checking a vehicle into the parking lot. "
                        +
                        "Requires a bearer token. Access restricted to Role='ADMIN'", security = @SecurityRequirement(name = "security"), responses = {
                                        @ApiResponse(responseCode = "201", description = "Resource created successfully", headers = @Header(name = HttpHeaders.LOCATION, description = "URL to access the created resource"), content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ParkingLotResponseDto.class))),
                                        @ApiResponse(responseCode = "404", description = "Possible causes: <br/>" +
                                                        "- Client's CPF not registered in the system; <br/>" +
                                                        "- No free parking space found;", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                                        @ApiResponse(responseCode = "422", description = "Resource not processed due to missing or invalid data", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                                        @ApiResponse(responseCode = "403", description = "Resource not allowed for CLIENT profile", content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
                        })
        @PostMapping("/check-in")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ParkingLotResponseDto> checkIn(@RequestBody @Valid ParkingLotCreateDto dto) {
                ClientParkingSpace clientParkingSpace = ClientParkingSpaceMapper.toClientParkingSpace(dto);
                parkingLotService.checkIn(clientParkingSpace);
                ParkingLotResponseDto responseDto = ClientParkingSpaceMapper.toDto(clientParkingSpace);
                URI location = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{receipt}")
                                .buildAndExpand(clientParkingSpace.getReceipt()).toUri();
                return ResponseEntity.created(location).body(responseDto);
        }

        @Operation(summary = "Locate a parked vehicle", description = "Resource to return a parked vehicle " +
                        "by receipt number. Request requires the use of a bearer token.", security = @SecurityRequirement(name = "security"), parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "recibo", description = "Receipt number generated at check-in")
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Resource located successfully", content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ParkingLotResponseDto.class))),
                                        @ApiResponse(responseCode = "404", description = "Receipt number not found.", content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
                        })
        @GetMapping("/check-in/{receipt}")
        @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
        public ResponseEntity<ParkingLotResponseDto> findByReceipt(@PathVariable String receipt) {
                ClientParkingSpace clientParkingSpace = clientParkingSpaceService.findByReceipt(receipt);
                ParkingLotResponseDto responseDto = ClientParkingSpaceMapper.toDto(clientParkingSpace);
                return ResponseEntity.ok(responseDto);
        }

        @Operation(summary = "Check-out operation", description = "Resource for checking out a vehicle from the parking lot. "
                        +
                        "Request requires the use of a bearer token. Access restricted to Role='ADMIN'", security = @SecurityRequirement(name = "security"), parameters = {
                                        @Parameter(in = ParameterIn.PATH, name = "recibo", description = "Receipt number generated at check-in", required = true)
                        }, responses = {
                                        @ApiResponse(responseCode = "200", description = "Resource updated successfully", content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ParkingLotResponseDto.class))),
                                        @ApiResponse(responseCode = "404", description = "Receipt number does not exist or "
                                                        +
                                                        "the vehicle has already been checked out.", content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                                        @ApiResponse(responseCode = "403", description = "Resource not allowed for CLIENT profile", content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
                        })
        @PutMapping("/check-out/{receipt}")
        @PreAuthorize("hasRole('ADMIN')")
        public ResponseEntity<ParkingLotResponseDto> checkOut(@PathVariable String receipt) {
                ClientParkingSpace clientParkingSpace = parkingLotService.checkOut(receipt);
                ParkingLotResponseDto responseDto = ClientParkingSpaceMapper.toDto(clientParkingSpace);
                return ResponseEntity.ok(responseDto);
        }
}
