package com.github.martinfrank.elitegames.backend.dto;

import java.util.List;

public record UserResponse(
    Long id,
    String username,
    String email,
    String firstName,
    String lastName,
    List<String> roles
) {}
