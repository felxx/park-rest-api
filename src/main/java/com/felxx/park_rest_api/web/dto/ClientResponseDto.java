package com.felxx.park_rest_api.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ClientResponseDto {
    
    private Long id;
    private String name;
    private String cpf;
}
