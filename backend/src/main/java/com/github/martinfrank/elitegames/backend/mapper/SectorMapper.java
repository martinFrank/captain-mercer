package com.github.martinfrank.elitegames.backend.mapper;

import com.github.martinfrank.elitegames.backend.dto.SectorResponse;
import com.github.martinfrank.elitegames.backend.game.SectorEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SectorMapper {

    private final StarMapper starMapper;

    public SectorMapper(StarMapper starMapper) {
        this.starMapper = starMapper;
    }

    public SectorResponse toResponse(SectorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new SectorResponse(
            entity.getId(),
            entity.getName(),
            entity.getWidth(),
            entity.getHeight(),
            entity.getStars() != null
                ? entity.getStars().stream().map(starMapper::toResponse).toList()
                : Collections.emptyList()
        );
    }
}
