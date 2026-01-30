package com.github.martinfrank.elitegames.backend.game;

import java.util.List;

public class GameDTO {
    public static class PositionDTO {
        public double x;
        public double y;
    }

    public static class EquipmentDTO {
        public String id;
        public String name;
        public String status;
    }

    public static class ShipDTO {
        public String name;
        public double weight;
        public int crewSize;
        public PositionDTO position; // Use nested class or flatten?
        // Flattened is easier for backend, but frontend uses {x,y}.
        // I'll stick to nested for clean mapping.
        public List<EquipmentDTO> equipment;
    }

    public ShipDTO ship;
}
