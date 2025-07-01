package com.felxx.park_rest_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ParkingSpaceCreateDto {

    @NotBlank
    @Size(min = 4, max = 4, message = "Code must be exactly 4 characters long")
    private String code;

    @NotBlank
    @Pattern(regexp = "AVAILABLE|OCCUPIED", message = "Status must be either 'AVAILABLE' or 'OCCUPIED'")
    private String status;
}
