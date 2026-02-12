package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.SectorSummaryResponse;
import com.github.martinfrank.elitegames.backend.game.entity.SectorEntity;
import org.springframework.stereotype.Component;

@Component
public class SectorSummaryMapper {

    public SectorSummaryResponse toResponse(SectorEntity entity) {
        if (entity == null) {
            return null;
        }
        return new SectorSummaryResponse(
            entity.getId(),
            entity.getName(),
            entity.getGridX(),
            entity.getGridY()
        );
    }
}
