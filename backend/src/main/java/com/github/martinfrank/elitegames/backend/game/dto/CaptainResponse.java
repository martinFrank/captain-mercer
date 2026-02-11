package com.github.martinfrank.elitegames.backend.game.dto;

import java.util.List;

public record CaptainResponse(
    String id,
    String name,
    ShipResponse ship,
    List<QuestResponse> quests,
    List<SectorResponse> sectors
) {}
