package com.github.martinfrank.elitegames.backend.game.entity;

import jakarta.persistence.*;

@Entity
public class StarServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Enumerated(EnumType.STRING)
    private StarServiceType type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public StarServiceType getType() {
        return type;
    }

    public void setType(StarServiceType type) {
        this.type = type;
    }
}
