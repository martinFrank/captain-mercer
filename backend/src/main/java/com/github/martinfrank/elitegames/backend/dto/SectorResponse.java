package com.github.martinfrank.elitegames.backend.dto;

import java.util.List;

public record SectorResponse(
    String id,
    String name,
    double width,
    double height,
    List<StarResponse> stars
) {}
