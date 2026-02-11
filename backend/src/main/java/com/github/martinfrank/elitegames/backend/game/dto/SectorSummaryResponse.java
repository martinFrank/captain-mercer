package com.github.martinfrank.elitegames.backend.game.dto;

public record SectorSummaryResponse(
    String id,
    String name,
    int gridX,
    int gridY
) {}
