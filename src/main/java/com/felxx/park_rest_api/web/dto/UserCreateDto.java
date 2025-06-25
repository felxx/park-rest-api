package com.felxx.park_rest_api.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserCreateDto {
    
    @Email(message = "Invalid email format", regexp = "^[a-z0-9.+-]+@[a-z0-9.-]+\\.[a-z]{2,}$") 
    @NotBlank(message = "Email cannot be blank")
    private String username;
    
    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, max = 6, message = "Password must be at least 6 characters long")
    private String password;

}
