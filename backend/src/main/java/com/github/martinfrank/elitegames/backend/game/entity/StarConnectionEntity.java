package com.github.martinfrank.elitegames.backend.game.entity;

import jakarta.persistence.*;

@Entity
public class StarConnectionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne
    @JoinColumn(name = "star_from_id")
    private StarEntity starFrom;

    @ManyToOne
    @JoinColumn(name = "star_to_id")
    private StarEntity starTo;

    private double distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StarEntity getStarFrom() {
        return starFrom;
    }

    public void setStarFrom(StarEntity starFrom) {
        this.starFrom = starFrom;
    }

    public StarEntity getStarTo() {
        return starTo;
    }

    public void setStarTo(StarEntity starTo) {
        this.starTo = starTo;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }
}
