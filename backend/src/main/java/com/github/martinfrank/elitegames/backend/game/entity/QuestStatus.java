package com.github.martinfrank.elitegames.backend.game.entity;

public enum QuestStatus {
    ACTIVE,
    COMPLETED;

    public static QuestStatus fromString(String status) {
        if (status == null) {
            return ACTIVE;
        }
        return switch (status.toLowerCase()) {
            case "completed" -> COMPLETED;
            default -> ACTIVE;
        };
    }

    public String toApiString() {
        return name().toLowerCase();
    }
}
