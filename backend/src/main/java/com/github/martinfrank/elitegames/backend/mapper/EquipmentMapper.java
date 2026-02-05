package com.github.martinfrank.elitegames.backend.mapper;

import com.github.martinfrank.elitegames.backend.dto.EquipmentResponse;
import com.github.martinfrank.elitegames.backend.game.EquipmentEntity;
import org.springframework.stereotype.Component;

@Component
public class EquipmentMapper {

    public EquipmentResponse toResponse(EquipmentEntity entity) {
        if (entity == null) {
            return null;
        }
        return new EquipmentResponse(
            entity.getId(),
            entity.getName(),
            entity.getStatusAsString()
        );
    }
}
