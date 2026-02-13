package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.game.entity.StarConnectionEntity;
import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StarConnectionServiceTest {

    private final StarPlacementService starPlacementService = new StarPlacementService();
    private final StarConnectionService starConnectionService = new StarConnectionService();

    @Test
    void allStarsReachable() {
        int iterations = 100;
        double width = 1000.0;
        double height = 1000.0;
        int starCount = 15;

        for (int i = 0; i < iterations; i++) {
            List<StarEntity> stars = starPlacementService.generateStars(width, height, starCount);
            List<StarConnectionEntity> connections = starConnectionService.generateConnections(stars);

            Set<StarEntity> reachable = findReachableStars(stars.getFirst(), connections);

            assertEquals(stars.size(), reachable.size(),
                    "Iteration " + i + ": Not all stars reachable. Reachable: " + reachable.size()
                            + " / " + stars.size() + ". Star positions: " + formatStarPositions(stars));
        }
    }

    private Set<StarEntity> findReachableStars(StarEntity start, List<StarConnectionEntity> connections) {
        Map<StarEntity, List<StarEntity>> adjacency = new HashMap<>();
        for (StarConnectionEntity conn : connections) {
            adjacency.computeIfAbsent(conn.getStarFrom(), k -> new ArrayList<>()).add(conn.getStarTo());
            adjacency.computeIfAbsent(conn.getStarTo(), k -> new ArrayList<>()).add(conn.getStarFrom());
        }

        Set<StarEntity> visited = new HashSet<>();
        Deque<StarEntity> queue = new ArrayDeque<>();
        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            StarEntity current = queue.poll();
            for (StarEntity neighbor : adjacency.getOrDefault(current, List.of())) {
                if (visited.add(neighbor)) {
                    queue.add(neighbor);
                }
            }
        }
        return visited;
    }

    private String formatStarPositions(List<StarEntity> stars) {
        StringBuilder sb = new StringBuilder("[");
        for (StarEntity star : stars) {
            sb.append(String.format("(%s: %.1f, %.1f) ", star.getName(), star.getX(), star.getY()));
        }
        sb.append("]");
        return sb.toString();
    }
}
