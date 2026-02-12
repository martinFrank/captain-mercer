package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.game.dto.GameRequest;
import com.github.martinfrank.elitegames.backend.game.entity.*;
import com.github.martinfrank.elitegames.backend.user.entity.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final SectorService sectorService;

    public GameService(GameRepository gameRepository, SectorService sectorService) {
        this.gameRepository = gameRepository;
        this.sectorService = sectorService;
    }

    @Transactional
    public GameEntity getOrCreateGame(UserEntity user) {
        GameEntity existingGame = gameRepository.findByUser_Id(user.getId());
        if (existingGame != null) {
            return existingGame;
        }
        return createNewGame(user);
    }

    // Integration method - orchestrates new game creation
    private GameEntity createNewGame(UserEntity user) {
        GameEntity newGame = new GameEntity();
        newGame.setUser(user);

        List<SectorEntity> sectors = generateGalacticGrid();
        newGame.setSectors(sectors);

        SectorEntity middleSector = findSectorByGrid(sectors, 1, 1);
        ShipEntity ship = createDefaultShip(middleSector);
        newGame.setShip(ship);

        newGame.setQuests(createInitialQuests());

        return gameRepository.save(newGame);
    }

    // Integration method - generates 3x3 grid of sectors
    private List<SectorEntity> generateGalacticGrid() {
        List<SectorEntity> sectors = new ArrayList<>();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                SectorEntity sector = sectorService.generateAndSaveSector(1000.0, 1000.0, 15, x, y);
                sectors.add(sector);
            }
        }
        return sectors;
    }

    // Integration method - creates ship in a given sector
    private ShipEntity createDefaultShip(SectorEntity sector) {
        ShipEntity ship = new ShipEntity();
        ship.setName("Stellar Wind");
        ship.setWeight(45000);
        ship.setCrewSize(4);
        ship.setSector(sector);
        ship.setCurrentStar(findClosestStarToCenter(sector.getStars(), sector.getWidth(), sector.getHeight()));
        ship.setEquipment(createInitialEquipment());
        return ship;
    }

    @Transactional
    public GameEntity updateGame(UserEntity user, GameRequest context) {
        GameEntity game = gameRepository.findByUser_Id(user.getId());
        if (game == null) {
            // Should exist if logged in and called getOrCreateGame previously
            // But handle defensively
            game = getOrCreateGame(user);
        }

        // Update Ship
        if (context.ship != null) {
            if (game.getShip() == null) {
                game.setShip(new ShipEntity());
            }
            ShipEntity ship = game.getShip();
            ship.setName(context.ship.name);
            ship.setWeight(context.ship.weight);
            ship.setCrewSize(context.ship.crewSize);

            if (context.ship.currentStarId != null) {
                StarEntity star = findStarById(ship.getSector().getStars(), context.ship.currentStarId);
                if (star != null) {
                    ship.setCurrentStar(star);
                }
            }

            // Update Equipment (simple replacement for now, or merge?)
            // Simple approach: clear and re-add based on DTO
            if (context.ship.equipment != null) {
                // Determine if we should clear existing list or update by ID
                // Since user sends full state probably, clearing might be okay but IDs would
                // change
                // Better: update by ID if present, otherwise add, remove missing.
                // For MVP, just update existing by ID if provided, otherwise add

                List<EquipmentEntity> currentEquipment = ship.getEquipment();
                if (currentEquipment == null) {
                    currentEquipment = new ArrayList<>();
                    ship.setEquipment(currentEquipment);
                }

                // Map incoming DTO to entities
                for (GameRequest.EquipmentRequest dto : context.ship.equipment) {
                    // Try to find existing equipment by ID if provided
                    EquipmentEntity existing = null;
                    if (dto.id != null) {
                        existing = currentEquipment.stream()
                                .filter(e -> e.getId() != null && e.getId().equals(dto.id))
                                .findFirst().orElse(null);
                    }

                    if (existing != null) {
                        existing.setName(dto.name);
                        existing.setStatus(dto.status);
                    } else {
                        EquipmentEntity newEq = new EquipmentEntity();
                        // ID is generated, so ignore dto.id if not found or null
                        newEq.setName(dto.name);
                        newEq.setStatus(dto.status);
                        currentEquipment.add(newEq);
                    }
                }
                // Remove equipment not in DTO? Maybe later.
            }
        }

        checkQuestCompletion(game);

        return gameRepository.save(game);
    }

    // Integration method - orchestrates quest completion checks
    private void checkQuestCompletion(GameEntity game) {
        if (game.getQuests() == null || game.getShip() == null) {
            return;
        }
        for (QuestEntity quest : game.getQuests()) {
            if (quest.getStatus() == QuestStatus.ACTIVE && isEscapeQuestCompleted(quest, game.getShip())) {
                quest.setStatus(QuestStatus.COMPLETED);
            }
        }
    }

    // Operation method - pure logic, no calls to same class
    private boolean isEscapeQuestCompleted(QuestEntity quest, ShipEntity ship) {
        if (!"Flucht aus dem Palast".equals(quest.getTitle())) {
            return false;
        }
        if (ship.getCurrentStar() == null || ship.getSector() == null) {
            return false;
        }
        double centerX = ship.getSector().getWidth() / 2.0;
        double centerY = ship.getSector().getHeight() / 2.0;
        double dx = ship.getCurrentStar().getX() - centerX;
        double dy = ship.getCurrentStar().getY() - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance > 50.0;
    }

    // Operation method - finds sector by grid coordinates
    private SectorEntity findSectorByGrid(List<SectorEntity> sectors, int gridX, int gridY) {
        for (SectorEntity sector : sectors) {
            if (sector.getGridX() == gridX && sector.getGridY() == gridY) {
                return sector;
            }
        }
        return sectors.getFirst();
    }

    // Operation method - creates initial equipment list
    private List<EquipmentEntity> createInitialEquipment() {
        List<EquipmentEntity> equipment = new ArrayList<>();
        EquipmentEntity eq1 = new EquipmentEntity();
        eq1.setName("Ion Thrusters");
        eq1.setStatus("active");
        equipment.add(eq1);

        EquipmentEntity eq2 = new EquipmentEntity();
        eq2.setName("Deflector Shield");
        eq2.setStatus("active");
        equipment.add(eq2);
        return equipment;
    }

    // Operation method - finds star closest to sector center
    private StarEntity findClosestStarToCenter(List<StarEntity> stars, double sectorWidth, double sectorHeight) {
        double centerX = sectorWidth / 2.0;
        double centerY = sectorHeight / 2.0;
        StarEntity closest = null;
        double minDistance = Double.MAX_VALUE;
        for (StarEntity star : stars) {
            double dx = star.getX() - centerX;
            double dy = star.getY() - centerY;
            double distance = dx * dx + dy * dy;
            if (distance < minDistance) {
                minDistance = distance;
                closest = star;
            }
        }
        return closest;
    }

    // Operation method - finds star by ID in a list
    private StarEntity findStarById(List<StarEntity> stars, String starId) {
        for (StarEntity star : stars) {
            if (star.getId().equals(starId)) {
                return star;
            }
        }
        return null;
    }

    // Operation method - creates the initial quest list
    private List<QuestEntity> createInitialQuests() {
        List<QuestEntity> quests = new ArrayList<>();
        QuestEntity escapeQuest = new QuestEntity();
        escapeQuest.setTitle("Flucht aus dem Palast");
        escapeQuest.setDescription("Fliege zu einem anderen Stern, um dem Palast zu entkommen.");
        escapeQuest.setStatus(QuestStatus.ACTIVE);
        escapeQuest.setSortOrder(1);
        quests.add(escapeQuest);
        return quests;
    }
}
