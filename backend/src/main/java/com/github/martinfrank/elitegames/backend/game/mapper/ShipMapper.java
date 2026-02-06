package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.ShipResponse;
import com.github.martinfrank.elitegames.backend.game.entity.ShipEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ShipMapper {

    private final EquipmentMapper equipmentMapper;
    private final SectorMapper sectorMapper;

    public ShipMapper(EquipmentMapper equipmentMapper, SectorMapper sectorMapper) {
        this.equipmentMapper = equipmentMapper;
        this.sectorMapper = sectorMapper;
    }

    public ShipResponse toResponse(ShipEntity entity) {
        if (entity == null) {
            return null;
        }
        return new ShipResponse(
            entity.getId(),
            entity.getName(),
            entity.getWeight(),
            entity.getCrewSize(),
            entity.getCurrentStar() != null ? entity.getCurrentStar().getId() : null,
            entity.getCurrentStar() != null ? entity.getCurrentStar().getName() : null,
            entity.getEquipment() != null
                ? entity.getEquipment().stream().map(equipmentMapper::toResponse).toList()
                : Collections.emptyList(),
            sectorMapper.toResponse(entity.getSector())
        );
    }
}
