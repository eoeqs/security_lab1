package org.eoeqs.dtos;

import jakarta.validation.constraints.*;


public record RegisterRequest(
        @NotBlank(message = "Email cannot be blank")
        @Email(message = "Invalid email format") String email,
        @NotBlank(message = "Password cannot be blank") String password,
        @NotBlank(message = "Name cannot be blank") String name
) {}