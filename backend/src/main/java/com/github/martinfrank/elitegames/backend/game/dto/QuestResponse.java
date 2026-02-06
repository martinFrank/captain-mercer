package com.github.martinfrank.elitegames.backend.game.dto;

public record QuestResponse(
    String id,
    String title,
    String description,
    String status,
    int sortOrder
) {}
