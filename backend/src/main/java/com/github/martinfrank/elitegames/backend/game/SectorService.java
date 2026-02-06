package com.github.martinfrank.elitegames.backend.game;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class SectorService {

    private final SectorRepository sectorRepository;
    private final Random random = new Random();

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    public SectorEntity generateAndSaveSector(double width, double height, int starCount) {
        SectorEntity sector = generateSector(width, height, starCount);
        return sectorRepository.save(sector);
    }

    public SectorEntity generateSector(double width, double height, int starCount) {
        SectorEntity sector = new SectorEntity();
        sector.setName("Sector-" + UUID.randomUUID().toString().substring(0, 8));
        sector.setWidth(width);
        sector.setHeight(height);

        List<StarEntity> stars = generateStars(width, height, starCount);
        sector.setStars(stars);

        return sector;
    }

    private List<StarEntity> generateStars(double width, double height, int count) {
        List<StarEntity> stars = new ArrayList<>();

        // Stratified sampling to ensure even distribution (no clumps, no holes)
        double aspectRatio = width / height;
        // Calculate grid dimensions
        int cols = (int) Math.round(Math.sqrt(count * aspectRatio));
        int rows = (int) Math.round(Math.sqrt(count / aspectRatio));

        // Adjust rows/cols to ensure we have enough cells (at least count)
        if (cols * rows < count) {
            if (cols < rows)
                cols++;
            else
                rows++;
        }

        double cellWidth = width / cols;
        double cellHeight = height / rows;

        // Generate all possible cell indices
        List<int[]> cellIndices = new ArrayList<>();
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                cellIndices.add(new int[] { i, j });
            }
        }

        // Shuffle to fill random cells if we have more cells than count
        Collections.shuffle(cellIndices, random);

        // Fill cells
        for (int k = 0; k < count && k < cellIndices.size(); k++) {
            int[] pos = cellIndices.get(k);
            int i = pos[0];
            int j = pos[1];

            double cellX = i * cellWidth;
            double cellY = j * cellHeight;

            // Padding to avoid stars being too close to the grid lines
            double paddingX = cellWidth * 0.1;
            double paddingY = cellHeight * 0.1;

            double starX = cellX + paddingX + random.nextDouble() * (cellWidth - 2 * paddingX);
            double starY = cellY + paddingY + random.nextDouble() * (cellHeight - 2 * paddingY);

            StarEntity star = new StarEntity();
            star.setName("Star-" + UUID.randomUUID().toString().substring(0, 5));
            star.setX(starX);
            star.setY(starY);
            star.setType(generateRandomType());
            star.setSize(generateRandomSize());

            stars.add(star);
        }

        return stars;
    }

    private String generateRandomType() {
        String[] types = { "yellow", "blue", "red", "white" };
        return types[random.nextInt(types.length)];
    }

    private String generateRandomSize() {
        String[] sizes = { "small", "medium", "large" };
        return sizes[random.nextInt(sizes.length)];
    }
}
