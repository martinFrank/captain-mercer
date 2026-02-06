package com.github.martinfrank.elitegames.backend.game.dto;

public record StarConnectionResponse(
    String id,
    String starFromId,
    String starToId,
    double distance
) {}
