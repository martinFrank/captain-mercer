package com.github.martinfrank.elitegames.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

public record RegisterRequest(
        @NotBlank String username,
        @Size(min = 8) String password,
        @Email String email,
        String firstName,
        String lastName,
        List<String> roles
) {
}
