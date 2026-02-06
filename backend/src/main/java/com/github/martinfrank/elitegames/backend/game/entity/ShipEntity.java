package com.github.martinfrank.elitegames.backend.game.entity;

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

    @ManyToOne
    @JoinColumn(name = "current_star_id")
    private StarEntity currentStar;

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

    public StarEntity getCurrentStar() {
        return currentStar;
    }

    public void setCurrentStar(StarEntity currentStar) {
        this.currentStar = currentStar;
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
