package com.github.martinfrank.elitegames.backend.game.dto;

import java.util.List;

public record StarResponse(
    String id,
    String name,
    double x,
    double y,
    String type,
    String size,
    List<StarServiceResponse> services
) {}
