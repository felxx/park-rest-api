package com.felxx.park_rest_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class ParkingLotCreateDto {
    
    @NotBlank
    @Size(min = 8, max = 8)
    @Pattern(regexp = "[A-Z]{3}-[0-9]{4}", message = "License plate must be exactly 'XXX-0000' format")
    private String licensePlate;

    @NotBlank
    private String make;

    @NotBlank
    private String model;

    @NotBlank
    private String color;

    @NotBlank
    @Size(min = 11, max = 11, message = "CPF must be exactly 11 characters long")
    @CPF
    private String clientCpf;
}
