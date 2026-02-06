package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import com.github.martinfrank.elitegames.backend.game.entity.StarServiceEntity;
import com.github.martinfrank.elitegames.backend.game.entity.StarServiceType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
public class StarServiceAssigner {

    private final Random random = new Random();

    // ==================== INTEGRATION METHOD ====================

    public void assignServices(List<StarEntity> stars) {
        for (StarEntity star : stars) {
            star.setServices(createRandomServices());
        }
    }

    // ==================== OPERATION METHOD ====================

    private List<StarServiceEntity> createRandomServices() {
        List<StarServiceType> allTypes = new ArrayList<>(Arrays.asList(StarServiceType.values()));
        Collections.shuffle(allTypes, random);
        int count = 2 + random.nextInt(3);
        return allTypes.subList(0, count).stream().map(type -> {
            StarServiceEntity service = new StarServiceEntity();
            service.setType(type);
            return service;
        }).toList();
    }
}
