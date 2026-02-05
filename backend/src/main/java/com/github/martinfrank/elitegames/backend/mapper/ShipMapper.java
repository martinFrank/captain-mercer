package com.github.martinfrank.elitegames.backend.mapper;

import com.github.martinfrank.elitegames.backend.dto.ShipResponse;
import com.github.martinfrank.elitegames.backend.game.ShipEntity;
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
            entity.getX(),
            entity.getY(),
            entity.getEquipment() != null
                ? entity.getEquipment().stream().map(equipmentMapper::toResponse).toList()
                : Collections.emptyList(),
            sectorMapper.toResponse(entity.getSector())
        );
    }
}
