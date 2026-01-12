package com.github.martinfrank.elitegames.backend;


import com.github.martinfrank.elitegames.backend.user.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;

    public DataInitializer(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        if (userService.findByUsername("admin").isEmpty()) {
            //FIXME hardcoded name
            userService.register("admin", "admin123", List.of("ROLE_ADMIN"), "no.mail@reply.please", "firstname", "lastname");
            userService.register("martin", "geheim123", List.of("ROLE_USER"), "martin.frank.privat@googlemail.com", "martin", "frank");
        }
    }
}
