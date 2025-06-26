package com.felxx.park_rest_api.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserPasswordDto {

    @NotBlank(message = "Current password cannot be blank")
    private String currentPassword;
    @NotBlank(message = "New password cannot be blank")
    @Size(min = 6, max = 6, message = "New password must be at least 6 characters long")
    private String newPassword;
    @NotBlank(message = "Confirm password cannot be blank")
    private String confirmPassword;
}
