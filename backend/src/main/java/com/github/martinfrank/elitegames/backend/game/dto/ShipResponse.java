package com.github.martinfrank.elitegames.backend.game.dto;

import java.util.List;

public record ShipResponse(
    String id,
    String name,
    double weight,
    int crewSize,
    double x,
    double y,
    List<EquipmentResponse> equipment,
    SectorResponse sector
) {}
