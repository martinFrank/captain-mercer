package com.github.martinfrank.elitegames.backend.game.entity;

import com.github.martinfrank.elitegames.backend.user.entity.UserEntity;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserEntity user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ship_id", referencedColumnName = "id")
    private ShipEntity ship;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<QuestEntity> quests;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public ShipEntity getShip() {
        return ship;
    }

    public void setShip(ShipEntity ship) {
        this.ship = ship;
    }

    public List<QuestEntity> getQuests() {
        return quests;
    }

    public void setQuests(List<QuestEntity> quests) {
        this.quests = quests;
    }
}
