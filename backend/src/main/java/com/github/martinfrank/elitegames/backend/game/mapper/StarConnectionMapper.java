package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.StarConnectionResponse;
import com.github.martinfrank.elitegames.backend.game.entity.StarConnectionEntity;
import org.springframework.stereotype.Component;

@Component
public class StarConnectionMapper {

    public StarConnectionResponse toResponse(StarConnectionEntity entity) {
        if (entity == null) {
            return null;
        }
        return new StarConnectionResponse(
            entity.getId(),
            entity.getStarFrom() != null ? entity.getStarFrom().getId() : null,
            entity.getStarTo() != null ? entity.getStarTo().getId() : null,
            entity.getDistance()
        );
    }
}
