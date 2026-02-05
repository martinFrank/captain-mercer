package com.github.martinfrank.elitegames.backend.dto;

public record StarResponse(
    String id,
    String name,
    double x,
    double y,
    String type,
    String size
) {}
