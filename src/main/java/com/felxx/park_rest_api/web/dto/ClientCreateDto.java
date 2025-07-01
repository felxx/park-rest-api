package com.felxx.park_rest_api.web.dto;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class ClientCreateDto {
    
    @NotBlank
    @Size(min = 3, max = 100)
    private String name;

    @CPF
    @Size(min = 11, max = 11)
    private String cpf;
}
