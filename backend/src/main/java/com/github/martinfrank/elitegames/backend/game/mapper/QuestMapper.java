package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.QuestResponse;
import com.github.martinfrank.elitegames.backend.game.entity.QuestEntity;
import org.springframework.stereotype.Component;

@Component
public class QuestMapper {

    public QuestResponse toResponse(QuestEntity entity) {
        if (entity == null) {
            return null;
        }
        return new QuestResponse(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getStatusAsString(),
            entity.getSortOrder()
        );
    }
}
