package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class StarPlacementService {

    private static final double GRID_PADDING_FACTOR = 0.1;
    private static final String[] STAR_TYPES = {"yellow", "blue", "red", "white"};
    private static final String[] STAR_SIZES = {"small", "medium", "large"};

    private final Random random = new Random();

    // ==================== INTEGRATION METHODS ====================

    public List<StarEntity> generateStars(double width, double height, int count) {
        GridDimensions grid = calculateGridDimensions(width, height, count);
        List<int[]> cellIndices = createShuffledCellIndices(grid.cols(), grid.rows());
        return createStarsInCells(cellIndices, grid, count);
    }

    private List<StarEntity> createStarsInCells(List<int[]> cellIndices, GridDimensions grid, int count) {
        List<StarEntity> stars = new ArrayList<>();
        for (int k = 0; k < count && k < cellIndices.size(); k++) {
            int[] pos = cellIndices.get(k);
            StarEntity star = createStarInCell(pos[0], pos[1], grid);
            stars.add(star);
        }
        return stars;
    }

    // ==================== OPERATION METHODS ====================

    private GridDimensions calculateGridDimensions(double width, double height, int count) {
        double aspectRatio = width / height;
        int cols = (int) Math.round(Math.sqrt(count * aspectRatio));
        int rows = (int) Math.round(Math.sqrt(count / aspectRatio));

        if (cols * rows < count) {
            if (cols < rows) {
                cols++;
            } else {
                rows++;
            }
        }

        double cellWidth = width / cols;
        double cellHeight = height / rows;
        return new GridDimensions(cols, rows, cellWidth, cellHeight);
    }

    private List<int[]> createShuffledCellIndices(int cols, int rows) {
        List<int[]> cellIndices = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                cellIndices.add(new int[]{i, j});
            }
        }
        Collections.shuffle(cellIndices, random);
        return cellIndices;
    }

    private StarEntity createStarInCell(int col, int row, GridDimensions grid) {
        double cellX = col * grid.cellWidth();
        double cellY = row * grid.cellHeight();
        double paddingX = grid.cellWidth() * GRID_PADDING_FACTOR;
        double paddingY = grid.cellHeight() * GRID_PADDING_FACTOR;

        StarEntity star = new StarEntity();
        star.setName("Star-" + UUID.randomUUID().toString().substring(0, 5));
        star.setX(cellX + paddingX + random.nextDouble() * (grid.cellWidth() - 2 * paddingX));
        star.setY(cellY + paddingY + random.nextDouble() * (grid.cellHeight() - 2 * paddingY));
        star.setType(STAR_TYPES[random.nextInt(STAR_TYPES.length)]);
        star.setSize(STAR_SIZES[random.nextInt(STAR_SIZES.length)]);
        return star;
    }

    // ==================== HELPER RECORDS ====================

    private record GridDimensions(int cols, int rows, double cellWidth, double cellHeight) {}
}
