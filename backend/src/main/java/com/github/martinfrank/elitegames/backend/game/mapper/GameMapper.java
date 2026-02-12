package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.game.dto.CaptainResponse;
import com.github.martinfrank.elitegames.backend.game.dto.QuestResponse;
import com.github.martinfrank.elitegames.backend.game.dto.SectorResponse;
import com.github.martinfrank.elitegames.backend.game.entity.GameEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class GameMapper {

    private final ShipMapper shipMapper;
    private final QuestMapper questMapper;
    private final SectorMapper sectorMapper;

    public GameMapper(ShipMapper shipMapper, QuestMapper questMapper, SectorMapper sectorMapper) {
        this.shipMapper = shipMapper;
        this.questMapper = questMapper;
        this.sectorMapper = sectorMapper;
    }

    public CaptainResponse toResponse(GameEntity entity) {
        if (entity == null) {
            return null;
        }
        List<QuestResponse> quests = entity.getQuests() != null
            ? entity.getQuests().stream().map(questMapper::toResponse).toList()
            : Collections.emptyList();
        List<SectorResponse> sectors = entity.getSectors() != null
            ? entity.getSectors().stream().map(sectorMapper::toResponse).toList()
            : Collections.emptyList();
        return new CaptainResponse(
            entity.getId(),
            entity.getUser().getUsername(),
            shipMapper.toResponse(entity.getShip()),
            quests,
            sectors
        );
    }
}
