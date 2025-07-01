package com.felxx.park_rest_api.web.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.felxx.park_rest_api.entities.Client;
import com.felxx.park_rest_api.jwt.JwtUserDetails;
import com.felxx.park_rest_api.repositories.projection.ClientProjection;
import com.felxx.park_rest_api.services.ClientService;
import com.felxx.park_rest_api.services.UserService;
import com.felxx.park_rest_api.web.dto.ClientCreateDto;
import com.felxx.park_rest_api.web.dto.ClientResponseDto;
import com.felxx.park_rest_api.web.dto.PageableDto;
import com.felxx.park_rest_api.web.dto.UserResponseDto;
import com.felxx.park_rest_api.web.dto.mapper.ClientMapper;
import com.felxx.park_rest_api.web.dto.mapper.PageableMapper;
import com.felxx.park_rest_api.web.exceptions.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Tag(name = "Client")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {
    
    private final ClientService clientService;
    private final UserService userService;

    @Operation(summary = "Create a new client", security = @SecurityRequirement(name = "security"), responses = {
                @ApiResponse(responseCode = "201", description = "Client created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                @ApiResponse(responseCode = "409", description = "CPF already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "422", description = "Invalid input data", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                @ApiResponse(responseCode = "403", description = "ADMIN cannot create a client", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> create(@RequestBody @Valid ClientCreateDto clientCreateDto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = ClientMapper.toClient(clientCreateDto);
        client.setUser(userService.findById(userDetails.getId()));
        clientService.save(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClientMapper.toDto(client));
    }

    @Operation(summary = "Retrieve a client by his id", security = @SecurityRequirement(name = "security"), responses = {
            @ApiResponse(responseCode = "200", description = "Client succesfully found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "403", description = "User don't have permission to access this resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClientResponseDto> findById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }


    @Operation(summary = "Retrieve all users", security = @SecurityRequirement(name = "security"),
                parameters = {
                    @Parameter(in = ParameterIn.QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")), description = "Returned page"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")), description = "Total elements per page"
                    ),
                    @Parameter(in = ParameterIn.QUERY, name = "sort", hidden = true, array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "nome,asc")),
                            description = "Results ordenation.")
                },
                responses = {
                    @ApiResponse(responseCode = "200", description = "Users successfully found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User don't have permission to acess this resource", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PageableDto> findAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"name"}) Pageable pageable) {
        Page<ClientProjection> clients = clientService.findAll(pageable);
        return ResponseEntity.ok(PageableMapper.toDto(clients));
    }

        @Operation(summary = "Retrieve client details",
            description = "This endpoint retrieves the details of the authenticated client.",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Client details retrieved successfully",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClientResponseDto.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Access forbidden for non-client users",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
    @GetMapping("/details")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<ClientResponseDto> findDetails(@AuthenticationPrincipal JwtUserDetails userDetails) {
        Client client = clientService.findByUserId(userDetails.getId());
        return ResponseEntity.ok(ClientMapper.toDto(client));
    }
}
