package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.user.UserEntity;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final SectorRepository sectorRepository;

    public GameService(GameRepository gameRepository, SectorRepository sectorRepository) {
        this.gameRepository = gameRepository;
        this.sectorRepository = sectorRepository;
    }

    @Transactional
    public GameEntity getOrCreateGame(UserEntity user) {
        GameEntity existingGame = gameRepository.findByUser_Id(user.getId());
        if (existingGame != null) {
            // Ensure existing ships have a sector assigned
            if (existingGame.getShip() != null && existingGame.getShip().getSector() == null) {
                List<SectorEntity> sectors = sectorRepository.findAll();
                if (!sectors.isEmpty()) {
                    existingGame.getShip().setSector(sectors.getFirst());
                    gameRepository.save(existingGame);
                }
            }
            return existingGame;
        }

        // Create new game
        GameEntity newGame = new GameEntity();
        newGame.setUser(user);

        ShipEntity ship = new ShipEntity();
        ship.setName("Stellar Wind"); // Default name
        ship.setWeight(45000); // Default weight
        ship.setCrewSize(4); // Default crew
        ship.setX(500.0); // Start in middle
        ship.setY(500.0);

        // Assign default sector
        List<SectorEntity> sectors = sectorRepository.findAll();
        if (!sectors.isEmpty()) {
            ship.setSector(sectors.getFirst());
        }

        List<EquipmentEntity> initialEquipment = new ArrayList<>();
        EquipmentEntity eq1 = new EquipmentEntity();
        eq1.setName("Ion Thrusters");
        eq1.setStatus("active");
        initialEquipment.add(eq1);

        EquipmentEntity eq2 = new EquipmentEntity();
        eq2.setName("Deflector Shield");
        eq2.setStatus("active");
        initialEquipment.add(eq2);

        ship.setEquipment(initialEquipment);
        newGame.setShip(ship);

        return gameRepository.save(newGame);
    }

    @Transactional
    public GameEntity updateGame(UserEntity user, GameDTO context) {
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

            if (context.ship.position != null) {
                ship.setX(context.ship.position.x);
                ship.setY(context.ship.position.y);
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
                for (GameDTO.EquipmentDTO dto : context.ship.equipment) {
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

        return gameRepository.save(game);
    }
}
