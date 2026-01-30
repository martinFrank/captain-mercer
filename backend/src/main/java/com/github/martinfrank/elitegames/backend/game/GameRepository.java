package com.github.martinfrank.elitegames.backend.game;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<GameEntity, String> {
    GameEntity findByUser_Id(Long userId);
}
