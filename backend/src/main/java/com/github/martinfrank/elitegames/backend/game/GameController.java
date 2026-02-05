package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.user.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public ResponseEntity<GameEntity> getGame(@AuthenticationPrincipal UserEntity user) {
        LOGGER.info("GET /game");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        GameEntity game = gameService.getOrCreateGame(user);
        return ResponseEntity.ok(game);
    }

    @PostMapping
    public ResponseEntity<GameEntity> saveGame(@AuthenticationPrincipal UserEntity user, @RequestBody GameDTO gameDto) {
        LOGGER.info("POST /game");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        GameEntity updatedGame = gameService.updateGame(user, gameDto);
        return ResponseEntity.ok(updatedGame);
    }
}
