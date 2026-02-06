package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.SectorResponse;
import com.github.martinfrank.elitegames.backend.game.entity.SectorEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class SectorMapper {

    private final StarMapper starMapper;
    private final StarConnectionMapper starConnectionMapper;

    public SectorMapper(StarMapper starMapper, StarConnectionMapper starConnectionMapper) {
        this.starMapper = starMapper;
        this.starConnectionMapper = starConnectionMapper;
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
                : Collections.emptyList(),
            entity.getConnections() != null
                ? entity.getConnections().stream().map(starConnectionMapper::toResponse).toList()
                : Collections.emptyList()
        );
    }
}
