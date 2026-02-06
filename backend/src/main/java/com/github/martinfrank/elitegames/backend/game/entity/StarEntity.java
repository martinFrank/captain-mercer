package com.github.martinfrank.elitegames.backend.game.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class StarEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;
    private double x;
    private double y;
    private String type;
    private String size;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "star_id")
    private List<StarServiceEntity> services;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public List<StarServiceEntity> getServices() {
        return services;
    }

    public void setServices(List<StarServiceEntity> services) {
        this.services = services;
    }
}
