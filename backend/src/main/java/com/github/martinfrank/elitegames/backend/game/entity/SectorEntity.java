package com.github.martinfrank.elitegames.backend.game.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class SectorEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private double width;
    private double height;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sector_id")
    private List<StarEntity> stars;

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

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public List<StarEntity> getStars() {
        return stars;
    }

    public void setStars(List<StarEntity> stars) {
        this.stars = stars;
    }
}
