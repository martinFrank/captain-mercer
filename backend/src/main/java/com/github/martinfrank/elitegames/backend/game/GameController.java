package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.dto.CaptainResponse;
import com.github.martinfrank.elitegames.backend.dto.GameRequest;
import com.github.martinfrank.elitegames.backend.mapper.GameMapper;
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
    private final GameMapper gameMapper;

    public GameController(GameService gameService, GameMapper gameMapper) {
        this.gameService = gameService;
        this.gameMapper = gameMapper;
    }

    @GetMapping
    public ResponseEntity<CaptainResponse> getGame(@AuthenticationPrincipal UserEntity user) {
        LOGGER.info("GET /game");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        GameEntity game = gameService.getOrCreateGame(user);
        return ResponseEntity.ok(gameMapper.toResponse(game));
    }

    @PostMapping
    public ResponseEntity<CaptainResponse> saveGame(@AuthenticationPrincipal UserEntity user, @RequestBody GameRequest gameDto) {
        LOGGER.info("POST /game");
        if (user == null) {
            return ResponseEntity.status(401).build();
        }
        GameEntity updatedGame = gameService.updateGame(user, gameDto);
        return ResponseEntity.ok(gameMapper.toResponse(updatedGame));
    }
}
