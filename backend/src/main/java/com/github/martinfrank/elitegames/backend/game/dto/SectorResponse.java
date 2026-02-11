package com.github.martinfrank.elitegames.backend.game.dto;

import java.util.List;

public record SectorResponse(
    String id,
    String name,
    double width,
    double height,
    int gridX,
    int gridY,
    List<StarResponse> stars,
    List<StarConnectionResponse> connections
) {}
