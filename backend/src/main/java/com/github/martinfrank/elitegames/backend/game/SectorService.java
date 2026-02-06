package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.game.entity.SectorEntity;
import com.github.martinfrank.elitegames.backend.game.entity.SectorRepository;
import com.github.martinfrank.elitegames.backend.game.entity.StarConnectionEntity;
import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
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

        List<StarConnectionEntity> connections = generateConnections(stars);
        sector.setConnections(connections);

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

    /**
     * Generates connections between stars using Delaunay Triangulation (Bowyer-Watson algorithm).
     * This creates a planar graph where no edge crossings occur and maximizes minimum angles.
     */
    private List<StarConnectionEntity> generateConnections(List<StarEntity> stars) {
        List<StarConnectionEntity> connections = new ArrayList<>();

        if (stars == null || stars.size() < 2) {
            return connections;
        }

        // Handle special case of exactly 2 stars
        if (stars.size() == 2) {
            StarConnectionEntity connection = new StarConnectionEntity();
            connection.setStarFrom(stars.get(0));
            connection.setStarTo(stars.get(1));
            connection.setDistance(calculateDistance(stars.get(0), stars.get(1)));
            connections.add(connection);
            return connections;
        }

        // Convert stars to points for triangulation
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < stars.size(); i++) {
            StarEntity star = stars.get(i);
            points.add(new Point(star.getX(), star.getY(), i));
        }

        // Perform Delaunay triangulation using Bowyer-Watson algorithm
        List<Triangle> triangles = bowyerWatson(points);

        // Extract unique edges from triangles
        Set<Edge> edges = new HashSet<>();
        for (Triangle t : triangles) {
            edges.add(new Edge(t.p1.index, t.p2.index));
            edges.add(new Edge(t.p2.index, t.p3.index));
            edges.add(new Edge(t.p3.index, t.p1.index));
        }

        // Create connections from edges
        for (Edge edge : edges) {
            StarEntity from = stars.get(edge.i1);
            StarEntity to = stars.get(edge.i2);
            StarConnectionEntity connection = new StarConnectionEntity();
            connection.setStarFrom(from);
            connection.setStarTo(to);
            connection.setDistance(calculateDistance(from, to));
            connections.add(connection);
        }

        return connections;
    }

    /**
     * Bowyer-Watson algorithm for Delaunay triangulation.
     */
    private List<Triangle> bowyerWatson(List<Point> points) {
        List<Triangle> triangles = new ArrayList<>();

        // Find bounds of point set
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        for (Point p : points) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }

        // Create super-triangle that encompasses all points
        double dx = maxX - minX;
        double dy = maxY - minY;
        double deltaMax = Math.max(dx, dy) * 2;

        Point superP1 = new Point(minX - deltaMax, minY - deltaMax, -1);
        Point superP2 = new Point(minX + dx / 2, maxY + deltaMax, -2);
        Point superP3 = new Point(maxX + deltaMax, minY - deltaMax, -3);

        triangles.add(new Triangle(superP1, superP2, superP3));

        // Add each point one at a time
        for (Point point : points) {
            List<Triangle> badTriangles = new ArrayList<>();

            // Find all triangles whose circumcircle contains the point
            for (Triangle t : triangles) {
                if (t.circumcircleContains(point)) {
                    badTriangles.add(t);
                }
            }

            // Find the boundary of the polygonal hole
            List<Edge> polygon = new ArrayList<>();
            for (Triangle t : badTriangles) {
                Edge[] edges = {
                    new Edge(t.p1, t.p2),
                    new Edge(t.p2, t.p3),
                    new Edge(t.p3, t.p1)
                };
                for (Edge edge : edges) {
                    boolean shared = false;
                    for (Triangle other : badTriangles) {
                        if (other != t && other.containsEdge(edge)) {
                            shared = true;
                            break;
                        }
                    }
                    if (!shared) {
                        polygon.add(edge);
                    }
                }
            }

            // Remove bad triangles
            triangles.removeAll(badTriangles);

            // Create new triangles from the polygon edges to the new point
            for (Edge edge : polygon) {
                triangles.add(new Triangle(edge.p1, edge.p2, point));
            }
        }

        // Remove triangles that share vertices with super-triangle
        triangles.removeIf(t ->
            t.p1.index < 0 || t.p2.index < 0 || t.p3.index < 0
        );

        return triangles;
    }

    private double calculateDistance(StarEntity a, StarEntity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // Helper classes for Delaunay triangulation

    private static class Point {
        final double x, y;
        final int index; // -1, -2, -3 for super-triangle vertices

        Point(double x, double y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }
    }

    private static class Edge {
        final Point p1, p2;
        final int i1, i2; // for index-based edges

        Edge(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.i1 = -1;
            this.i2 = -1;
        }

        Edge(int i1, int i2) {
            this.p1 = null;
            this.p2 = null;
            // Normalize edge direction for consistent hashing
            this.i1 = Math.min(i1, i2);
            this.i2 = Math.max(i1, i2);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            if (p1 != null && p2 != null && edge.p1 != null && edge.p2 != null) {
                return (p1 == edge.p1 && p2 == edge.p2) || (p1 == edge.p2 && p2 == edge.p1);
            }
            return i1 == edge.i1 && i2 == edge.i2;
        }

        @Override
        public int hashCode() {
            if (p1 != null && p2 != null) {
                return System.identityHashCode(p1) + System.identityHashCode(p2);
            }
            return Objects.hash(i1, i2);
        }
    }

    private static class Triangle {
        final Point p1, p2, p3;
        private final double circumX, circumY, circumRadiusSq;

        Triangle(Point p1, Point p2, Point p3) {
            this.p1 = p1;
            this.p2 = p2;
            this.p3 = p3;

            // Calculate circumcircle
            double ax = p1.x, ay = p1.y;
            double bx = p2.x, by = p2.y;
            double cx = p3.x, cy = p3.y;

            double d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));

            if (Math.abs(d) < 1e-10) {
                // Degenerate triangle (collinear points)
                circumX = (ax + bx + cx) / 3;
                circumY = (ay + by + cy) / 3;
                circumRadiusSq = Double.MAX_VALUE;
            } else {
                double ax2ay2 = ax * ax + ay * ay;
                double bx2by2 = bx * bx + by * by;
                double cx2cy2 = cx * cx + cy * cy;

                circumX = (ax2ay2 * (by - cy) + bx2by2 * (cy - ay) + cx2cy2 * (ay - by)) / d;
                circumY = (ax2ay2 * (cx - bx) + bx2by2 * (ax - cx) + cx2cy2 * (bx - ax)) / d;

                double dx = ax - circumX;
                double dy = ay - circumY;
                circumRadiusSq = dx * dx + dy * dy;
            }
        }

        boolean circumcircleContains(Point p) {
            double dx = p.x - circumX;
            double dy = p.y - circumY;
            return dx * dx + dy * dy <= circumRadiusSq + 1e-10;
        }

        boolean containsEdge(Edge edge) {
            return (edge.p1 == p1 || edge.p1 == p2 || edge.p1 == p3) &&
                   (edge.p2 == p1 || edge.p2 == p2 || edge.p2 == p3);
        }
    }
}
