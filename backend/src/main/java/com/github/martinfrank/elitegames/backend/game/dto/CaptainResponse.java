package com.github.martinfrank.elitegames.backend.game.dto;

public record CaptainResponse(
    String id,
    String name,
    ShipResponse ship
) {}
