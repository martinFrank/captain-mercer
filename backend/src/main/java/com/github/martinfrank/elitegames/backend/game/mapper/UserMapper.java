package com.github.martinfrank.elitegames.backend.game.mapper;

import com.github.martinfrank.elitegames.backend.user.dto.UserResponse;
import com.github.martinfrank.elitegames.backend.user.entity.UserEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserMapper {

    public UserResponse toResponse(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserResponse(
            entity.getId(),
            entity.getUsername(),
            entity.getEmail(),
            entity.getFirstName(),
            entity.getLastName(),
            entity.getRoles() != null ? entity.getRoles() : Collections.emptyList()
        );
    }
}
