package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.game.entity.SectorEntity;
import com.github.martinfrank.elitegames.backend.game.entity.SectorRepository;
import com.github.martinfrank.elitegames.backend.game.entity.StarConnectionEntity;
import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;
    private final StarPlacementService starPlacementService;
    private final StarServiceAssigner starServiceAssigner;
    private final StarConnectionService starConnectionService;

    public SectorService(SectorRepository sectorRepository,
                         StarPlacementService starPlacementService,
                         StarServiceAssigner starServiceAssigner,
                         StarConnectionService starConnectionService) {
        this.sectorRepository = sectorRepository;
        this.starPlacementService = starPlacementService;
        this.starServiceAssigner = starServiceAssigner;
        this.starConnectionService = starConnectionService;
    }

    // ==================== INTEGRATION METHODS ====================

    public SectorEntity generateAndSaveSector(double width, double height, int starCount) {
        SectorEntity sector = generateSector(width, height, starCount);
        return sectorRepository.save(sector);
    }

    public SectorEntity generateSector(double width, double height, int starCount) {
        SectorEntity sector = createSectorEntity(width, height);
        List<StarEntity> stars = starPlacementService.generateStars(width, height, starCount);
        starServiceAssigner.assignServices(stars);
        List<StarConnectionEntity> connections = starConnectionService.generateConnections(stars);
        sector.setStars(stars);
        sector.setConnections(connections);
        return sector;
    }

    // ==================== OPERATION METHODS ====================

    private SectorEntity createSectorEntity(double width, double height) {
        SectorEntity sector = new SectorEntity();
        sector.setName("Sector-" + UUID.randomUUID().toString().substring(0, 8));
        sector.setWidth(width);
        sector.setHeight(height);
        return sector;
    }
}
