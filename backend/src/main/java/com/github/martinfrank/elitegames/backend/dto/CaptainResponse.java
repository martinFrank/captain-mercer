package com.github.martinfrank.elitegames.backend.dto;

public record CaptainResponse(
    String id,
    String name,
    ShipResponse ship
) {}
