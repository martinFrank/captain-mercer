package com.github.martinfrank.elitegames.backend.user;

import com.github.martinfrank.elitegames.backend.user.dto.UserResponse;
import com.github.martinfrank.elitegames.backend.game.mapper.UserMapper;
import com.github.martinfrank.elitegames.backend.user.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private final UserMapper userMapper;

    public UserController(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    //dieser endpoint ist nun für registrierte user erreichbar -> vgl SecurityConfig.securityFilterChain
    @GetMapping("/me")
    public UserResponse getMe(@AuthenticationPrincipal UserEntity user) {
        LOGGER.info("GET /me");
        return userMapper.toResponse(user);
    }

}
