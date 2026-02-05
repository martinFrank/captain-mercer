package com.github.martinfrank.elitegames.backend;

import com.github.martinfrank.elitegames.backend.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final com.github.martinfrank.elitegames.backend.game.SectorRepository sectorRepository;
    private final com.github.martinfrank.elitegames.backend.game.GameRepository gameRepository;

    public DataInitializer(UserService userService,
            com.github.martinfrank.elitegames.backend.game.SectorRepository sectorRepository,
            com.github.martinfrank.elitegames.backend.game.GameRepository gameRepository) {
        this.userService = userService;
        this.sectorRepository = sectorRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public void run(String... args) {
        if (userService.findByUsername("admin").isPresent()) {
            // FIXME hardcoded name
            userService.register("admin", "admin123", List.of("ROLE_ADMIN"), "no.mail@reply.please", "firstname",
                    "lastname");
            userService.register("martin", "geheim123", List.of("ROLE_USER"), "martin.frank.privat@googlemail.com",
                    "martin", "frank");
        }

        createDefaultSector();
    }

    private void createDefaultSector() {
        if (sectorRepository.count() == 0) {
            com.github.martinfrank.elitegames.backend.game.SectorEntity sector = new com.github.martinfrank.elitegames.backend.game.SectorEntity();
            sector.setName("Alpha Centauri");
            sector.setWidth(1000);
            sector.setHeight(1000);

            java.util.List<com.github.martinfrank.elitegames.backend.game.StarEntity> stars = new java.util.ArrayList<>();
            stars.add(createStar("Alpha A", 300, 300, "yellow", "large"));
            stars.add(createStar("Alpha B", 700, 600, "blue", "medium"));
            stars.add(createStar("Proxima", 500, 500, "red", "small"));
            stars.add(createStar("Rigel", 150, 150, "white", "large"));
            stars.add(createStar("Betelgeuse", 850, 200, "red", "large"));
            stars.add(createStar("Sirius", 200, 800, "white", "medium"));

            sector.setStars(stars);
            sectorRepository.save(sector);
        }
    }

    private com.github.martinfrank.elitegames.backend.game.StarEntity createStar(String name, double x, double y,
            String type, String size) {
        com.github.martinfrank.elitegames.backend.game.StarEntity star = new com.github.martinfrank.elitegames.backend.game.StarEntity();
        star.setName(name);
        star.setX(x);
        star.setY(y);
        star.setType(type);
        star.setSize(size);
        return star;
    }
}
