package com.github.martinfrank.elitegames.backend.mapper;

import com.github.martinfrank.elitegames.backend.dto.CaptainResponse;
import com.github.martinfrank.elitegames.backend.game.GameEntity;
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
