package com.github.martinfrank.elitegames.backend.game.entity;

public enum EquipmentStatus {
    ACTIVE,
    DAMAGED,
    OFFLINE;

    public static EquipmentStatus fromString(String status) {
        if (status == null) {
            return OFFLINE;
        }
        return switch (status.toLowerCase()) {
            case "active" -> ACTIVE;
            case "damaged" -> DAMAGED;
            default -> OFFLINE;
        };
    }

    public String toApiString() {
        return name().toLowerCase();
    }
}
