package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.CaptainResponse;
import com.github.martinfrank.elitegames.backend.game.entity.GameEntity;
import org.springframework.stereotype.Component;

@Component
public class GameMapper {

    private final ShipMapper shipMapper;

    public GameMapper(ShipMapper shipMapper) {
        this.shipMapper = shipMapper;
    }

    public CaptainResponse toResponse(GameEntity entity) {
        if (entity == null) {
            return null;
        }
        return new CaptainResponse(
            entity.getId(),
            entity.getUser().getUsername(),
            shipMapper.toResponse(entity.getShip())
        );
    }
}
