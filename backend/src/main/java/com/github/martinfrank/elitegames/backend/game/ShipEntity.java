package com.github.martinfrank.elitegames.backend.game;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class ShipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private double weight;
    private int crewSize;

    // Use a separate entity or embedded for position?
    // User requested "position (2d)"
    private double x;
    private double y;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<EquipmentEntity> equipment;

    @ManyToOne
    @JoinColumn(name = "sector_id")
    private SectorEntity sector;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getCrewSize() {
        return crewSize;
    }

    public void setCrewSize(int crewSize) {
        this.crewSize = crewSize;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public List<EquipmentEntity> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<EquipmentEntity> equipment) {
        this.equipment = equipment;
    }

    public SectorEntity getSector() {
        return sector;
    }

    public void setSector(SectorEntity sector) {
        this.sector = sector;
    }
}
