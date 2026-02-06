package com.github.martinfrank.elitegames.backend.game;

import com.github.martinfrank.elitegames.backend.game.entity.SectorEntity;
import com.github.martinfrank.elitegames.backend.game.entity.SectorRepository;
import com.github.martinfrank.elitegames.backend.game.entity.StarConnectionEntity;
import com.github.martinfrank.elitegames.backend.game.entity.StarEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

@Service
public class SectorService {

    private static final double GRID_PADDING_FACTOR = 0.1;
    private static final String[] STAR_TYPES = {"yellow", "blue", "red", "white"};
    private static final String[] STAR_SIZES = {"small", "medium", "large"};

    private final SectorRepository sectorRepository;
    private final Random random = new Random();

    public SectorService(SectorRepository sectorRepository) {
        this.sectorRepository = sectorRepository;
    }

    // ==================== INTEGRATION METHODS ====================

    public SectorEntity generateAndSaveSector(double width, double height, int starCount) {
        SectorEntity sector = generateSector(width, height, starCount);
        return sectorRepository.save(sector);
    }

    public SectorEntity generateSector(double width, double height, int starCount) {
        SectorEntity sector = createSectorEntity(width, height);
        List<StarEntity> stars = generateStars(width, height, starCount);
        List<StarConnectionEntity> connections = generateConnections(stars);

        sector.setStars(stars);
        sector.setConnections(connections);
        return sector;
    }

    private List<StarEntity> generateStars(double width, double height, int count) {
        GridDimensions grid = calculateGridDimensions(width, height, count);
        List<int[]> cellIndices = createShuffledCellIndices(grid.cols(), grid.rows());
        return createStarsInCells(cellIndices, grid, count);
    }

    private List<StarConnectionEntity> generateConnections(List<StarEntity> stars) {
        if (stars == null || stars.size() < 2) {
            return new ArrayList<>();
        }
        if (stars.size() == 2) {
            return createSingleConnection(stars.get(0), stars.get(1));
        }

        List<Point> points = convertStarsToPoints(stars);
        List<Triangle> triangles = performDelaunayTriangulation(points);
        Set<Edge> edges = extractEdgesFromTriangles(triangles);
        List<StarConnectionEntity> connections = createConnectionsFromEdges(edges, stars);
        return pruneConnections(connections);
    }

    private List<Triangle> performDelaunayTriangulation(List<Point> points) {
        Bounds bounds = calculateBounds(points);
        Triangle superTriangle = createSuperTriangle(bounds);
        List<Triangle> triangles = buildTriangulation(points, superTriangle);
        return removeSuperTriangleVertices(triangles);
    }

    private List<StarConnectionEntity> pruneConnections(List<StarConnectionEntity> connections) {
        Map<StarEntity, List<StarConnectionEntity>> adjacency = buildAdjacencyMap(connections);
        Set<StarConnectionEntity> toRemove = findLongestConnectionsToRemove(adjacency);
        return removeConnections(connections, toRemove);
    }

    // ==================== OPERATION METHODS ====================

    private SectorEntity createSectorEntity(double width, double height) {
        SectorEntity sector = new SectorEntity();
        sector.setName("Sector-" + UUID.randomUUID().toString().substring(0, 8));
        sector.setWidth(width);
        sector.setHeight(height);
        return sector;
    }

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

    private List<StarEntity> createStarsInCells(List<int[]> cellIndices, GridDimensions grid, int count) {
        List<StarEntity> stars = new ArrayList<>();
        for (int k = 0; k < count && k < cellIndices.size(); k++) {
            int[] pos = cellIndices.get(k);
            StarEntity star = createStarInCell(pos[0], pos[1], grid);
            stars.add(star);
        }
        return stars;
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

    private List<StarConnectionEntity> createSingleConnection(StarEntity from, StarEntity to) {
        List<StarConnectionEntity> connections = new ArrayList<>();
        StarConnectionEntity connection = new StarConnectionEntity();
        connection.setStarFrom(from);
        connection.setStarTo(to);
        connection.setDistance(calculateDistance(from, to));
        connections.add(connection);
        return connections;
    }

    private List<Point> convertStarsToPoints(List<StarEntity> stars) {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < stars.size(); i++) {
            StarEntity star = stars.get(i);
            points.add(new Point(star.getX(), star.getY(), i));
        }
        return points;
    }

    private Bounds calculateBounds(List<Point> points) {
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        for (Point p : points) {
            minX = Math.min(minX, p.x);
            minY = Math.min(minY, p.y);
            maxX = Math.max(maxX, p.x);
            maxY = Math.max(maxY, p.y);
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    private Triangle createSuperTriangle(Bounds bounds) {
        double dx = bounds.maxX() - bounds.minX();
        double dy = bounds.maxY() - bounds.minY();
        double deltaMax = Math.max(dx, dy) * 2;

        Point superP1 = new Point(bounds.minX() - deltaMax, bounds.minY() - deltaMax, -1);
        Point superP2 = new Point(bounds.minX() + dx / 2, bounds.maxY() + deltaMax, -2);
        Point superP3 = new Point(bounds.maxX() + deltaMax, bounds.minY() - deltaMax, -3);
        return new Triangle(superP1, superP2, superP3);
    }

    private List<Triangle> buildTriangulation(List<Point> points, Triangle superTriangle) {
        List<Triangle> triangles = new ArrayList<>();
        triangles.add(superTriangle);

        for (Point point : points) {
            List<Triangle> badTriangles = findBadTriangles(triangles, point);
            List<Edge> polygonHole = findPolygonHole(badTriangles);
            triangles.removeAll(badTriangles);
            addNewTriangles(triangles, polygonHole, point);
        }
        return triangles;
    }

    private List<Triangle> findBadTriangles(List<Triangle> triangles, Point point) {
        List<Triangle> badTriangles = new ArrayList<>();
        for (Triangle t : triangles) {
            if (t.circumcircleContains(point)) {
                badTriangles.add(t);
            }
        }
        return badTriangles;
    }

    private List<Edge> findPolygonHole(List<Triangle> badTriangles) {
        List<Edge> polygon = new ArrayList<>();
        for (Triangle t : badTriangles) {
            Edge[] edges = {
                new Edge(t.p1, t.p2),
                new Edge(t.p2, t.p3),
                new Edge(t.p3, t.p1)
            };
            for (Edge edge : edges) {
                if (!isEdgeShared(edge, t, badTriangles)) {
                    polygon.add(edge);
                }
            }
        }
        return polygon;
    }

    private boolean isEdgeShared(Edge edge, Triangle current, List<Triangle> triangles) {
        for (Triangle other : triangles) {
            if (other != current && other.containsEdge(edge)) {
                return true;
            }
        }
        return false;
    }

    private void addNewTriangles(List<Triangle> triangles, List<Edge> polygonHole, Point point) {
        for (Edge edge : polygonHole) {
            triangles.add(new Triangle(edge.p1, edge.p2, point));
        }
    }

    private List<Triangle> removeSuperTriangleVertices(List<Triangle> triangles) {
        triangles.removeIf(t -> t.p1.index < 0 || t.p2.index < 0 || t.p3.index < 0);
        return triangles;
    }

    private Set<Edge> extractEdgesFromTriangles(List<Triangle> triangles) {
        Set<Edge> edges = new HashSet<>();
        for (Triangle t : triangles) {
            edges.add(new Edge(t.p1.index, t.p2.index));
            edges.add(new Edge(t.p2.index, t.p3.index));
            edges.add(new Edge(t.p3.index, t.p1.index));
        }
        return edges;
    }

    private List<StarConnectionEntity> createConnectionsFromEdges(Set<Edge> edges, List<StarEntity> stars) {
        List<StarConnectionEntity> connections = new ArrayList<>();
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

    private Map<StarEntity, List<StarConnectionEntity>> buildAdjacencyMap(List<StarConnectionEntity> connections) {
        Map<StarEntity, List<StarConnectionEntity>> adjacency = new HashMap<>();
        for (StarConnectionEntity conn : connections) {
            adjacency.computeIfAbsent(conn.getStarFrom(), k -> new ArrayList<>()).add(conn);
            adjacency.computeIfAbsent(conn.getStarTo(), k -> new ArrayList<>()).add(conn);
        }
        return adjacency;
    }

    private Set<StarConnectionEntity> findLongestConnectionsToRemove(
            Map<StarEntity, List<StarConnectionEntity>> adjacency) {
        Set<StarConnectionEntity> toRemove = new HashSet<>();
        for (List<StarConnectionEntity> starConnections : adjacency.values()) {
            if (starConnections.size() > 1) {
                starConnections.stream()
                    .max(Comparator.comparingDouble(StarConnectionEntity::getDistance))
                    .ifPresent(toRemove::add);
            }
        }
        return toRemove;
    }

    private List<StarConnectionEntity> removeConnections(
            List<StarConnectionEntity> connections, Set<StarConnectionEntity> toRemove) {
        List<StarConnectionEntity> pruned = new ArrayList<>(connections);
        pruned.removeAll(toRemove);
        return pruned;
    }

    private double calculateDistance(StarEntity a, StarEntity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    // ==================== HELPER RECORDS ====================

    private record GridDimensions(int cols, int rows, double cellWidth, double cellHeight) {}

    private record Bounds(double minX, double minY, double maxX, double maxY) {}

    // ==================== HELPER CLASSES FOR TRIANGULATION ====================

    private static class Point {
        final double x, y;
        final int index;

        Point(double x, double y, int index) {
            this.x = x;
            this.y = y;
            this.index = index;
        }
    }

    private static class Edge {
        final Point p1, p2;
        final int i1, i2;

        Edge(Point p1, Point p2) {
            this.p1 = p1;
            this.p2 = p2;
            this.i1 = -1;
            this.i2 = -1;
        }

        Edge(int i1, int i2) {
            this.p1 = null;
            this.p2 = null;
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

            double ax = p1.x, ay = p1.y;
            double bx = p2.x, by = p2.y;
            double cx = p3.x, cy = p3.y;

            double d = 2 * (ax * (by - cy) + bx * (cy - ay) + cx * (ay - by));

            if (Math.abs(d) < 1e-10) {
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
