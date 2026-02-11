package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.StarResponse;
import com.github.martinfrank.elitegames.backend.game.dto.StarServiceResponse;
import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import com.github.martinfrank.elitegames.backend.game.entity.StarServiceEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

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
            entity.getSize(),
            mapServices(entity.getServices())
        );
    }

    private List<StarServiceResponse> mapServices(List<StarServiceEntity> services) {
        if (services == null) {
            return Collections.emptyList();
        }
        return services.stream()
            .map(s -> new StarServiceResponse(s.getId(), s.getType().name()))
            .toList();
    }
}
