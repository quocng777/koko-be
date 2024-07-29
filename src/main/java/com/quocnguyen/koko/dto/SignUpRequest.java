package com.quocnguyen.koko.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class SignUpRequest {
    @NotBlank(message = "User's name is required")
    private String name;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "User's email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @Min(value = 0, message = "Gender must be 0 [Male] or 1 [Female]")
    @Max(value = 1, message = "Gender must be 0 [Male] or 1 [Female]")
    private int gender;

    @NotNull(message = "Birthdate is required")
    private Date birthdate;
}
