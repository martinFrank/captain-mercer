package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.StarResponse;
import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import org.springframework.stereotype.Component;

@Component
public class StarMapper {

    public StarResponse toResponse(StarEntity entity) {
        if (entity == null) {
            return null;
        }
        return new StarResponse(
            entity.getId(),
            entity.getName(),
            entity.getX(),
            entity.getY(),
            entity.getType(),
            entity.getSize()
        );
    }
}
