package com.github.martinfrank.elitegames.backend.game.dto;

import java.util.List;

public class GameRequest {
    public static class PositionRequest {
        public double x;
        public double y;
    }

    public static class EquipmentRequest {
        public String id;
        public String name;
        public String status;
    }

    public static class ShipRequest {
        public String name;
        public double weight;
        public int crewSize;
        public PositionRequest position;
        public List<EquipmentRequest> equipment;
    }

    public ShipRequest ship;
}
