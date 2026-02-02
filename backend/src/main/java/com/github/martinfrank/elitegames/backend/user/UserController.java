package com.github.martinfrank.elitegames.backend.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    //dieser endpoint ist nun für registrierte user erreichbar -> vgl SecurityConfig.securityFilterChain
    @GetMapping("/me")
    public UserEntity getMe(@AuthenticationPrincipal UserEntity user) {
        LOGGER.info("GET /me");
        return user;
    }

}
