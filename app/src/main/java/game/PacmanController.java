package game;
import Solids.Pacman;
import java.util.*;

public class PacmanController {
    private Pacman pacman;
    private Maze maze;

    // Current path as a list of grid cells (each cell represented by a Point)
    private List<Point> path;

    // Recalculate path every 2 seconds
    private float pathRecalcInterval = 2.0f;
    private float timeSinceLastPath = 0;

    public PacmanController(Pacman pacman, Maze maze) {
        this.pacman = pacman;
        this.maze = maze;
        this.path = new ArrayList<>();
    }

    // Call update() every frame with deltaTime and the player's (camera) world position.
    public void update(float deltaTime, float playerX, float playerZ) {
        timeSinceLastPath += deltaTime;
        int[][] grid = maze.getMazeGrid(); // Make sure you add a getter in Maze.java: public int[][] getMazeGrid();
        int rows = grid.length;
        int cols = grid[0].length;
        float tileSize = 1.0f;  // Adjust if your Maze uses a different tileSize

        // Convert Pacman and player world positions to grid cell indices.
        Point pacmanCell = worldToGrid(pacman.getX(), pacman.getZ(), cols, tileSize);
        Point playerCell = worldToGrid(playerX, playerZ, cols, tileSize);

        // Only recalc the path every pathRecalcInterval seconds.
        if (timeSinceLastPath >= pathRecalcInterval) {
            path = findPath(pacmanCell, playerCell, grid);
            timeSinceLastPath = 0;
        }

        // Move along the path slowly.
        if (!path.isEmpty()) {
            Point nextCell = path.get(0);
            // Compute the center world coordinates for the next cell.
            float targetX = (nextCell.col - (cols / 2)) * tileSize + tileSize / 2;
            float targetZ = (nextCell.row - (rows / 2)) * tileSize + tileSize / 2;

            float dx = targetX - pacman.getX();
            float dz = targetZ - pacman.getZ();
            float distance = (float)Math.sqrt(dx * dx + dz * dz);

            // When very close, remove this cell from the path.
            if (distance < 0.1f) {
                path.remove(0);
            } else {
                float move = pacman.getSpeed() * deltaTime;
                if (move > distance) {
                    move = distance;
                }
                float newX = pacman.getX() + (dx / distance) * move;
                float newZ = pacman.getZ() + (dz / distance) * move;
                pacman.setPosition(newX, pacman.getY(), newZ);
            }
        }
    }

    // Helper to convert world position to grid cell indices.
    private Point worldToGrid(float worldX, float worldZ, int cols, float tileSize) {
        // Assuming maze is centered with worldX = (col - (cols/2))*tileSize.
        int col = (int)Math.floor((worldX + (cols / 2) * tileSize) / tileSize);
        int row = (int)Math.floor((worldZ + (cols / 2) * tileSize) / tileSize);
        return new Point(row, col);
    }

    // A* pathfinding method returning a list of grid cells (Points) from start to goal.
    private List<Point> findPath(Point start, Point goal, int[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        boolean[][] closed = new boolean[rows][cols];
        PriorityQueue<Node> open = new PriorityQueue<>(Comparator.comparingDouble(n -> n.f));
        Node[][] allNodes = new Node[rows][cols];

        Node startNode = new Node(start.row, start.col, null, 0, heuristic(start, goal));
        open.add(startNode);
        allNodes[start.row][start.col] = startNode;

        while (!open.isEmpty()) {
            Node current = open.poll();
            if (current.row == goal.row && current.col == goal.col) {
                return reconstructPath(current);
            }
            closed[current.row][current.col] = true;

            // Explore neighbors: up, down, left, right.
            int[][] directions = { { -1, 0 }, { 1, 0 }, { 0, -1 }, { 0, 1 } };
            for (int[] dir : directions) {
                int newRow = current.row + dir[0];
                int newCol = current.col + dir[1];

                // Out of bounds or blocked cell.
                if (newRow < 0 || newRow >= rows || newCol < 0 || newCol >= cols)
                    continue;
                if (grid[newRow][newCol] != 0)
                    continue;
                if (closed[newRow][newCol])
                    continue;

                float newG = current.g + 1; // Assume cost = 1 for adjacent moves.
                Node neighbor = allNodes[newRow][newCol];
                if (neighbor == null) {
                    neighbor = new Node(newRow, newCol, current, newG, heuristic(new Point(newRow, newCol), goal));
                    allNodes[newRow][newCol] = neighbor;
                    open.add(neighbor);
                } else if (newG < neighbor.g) {
                    // Found a better path.
                    neighbor.g = newG;
                    neighbor.f = newG + neighbor.h;
                    neighbor.parent = current;
                    open.remove(neighbor);
                    open.add(neighbor);
                }
            }
        }
        // No path found.
        return new ArrayList<>();
    }

    // Reconstructs the found path from the goal node.
    private List<Point> reconstructPath(Node current) {
        List<Point> path = new ArrayList<>();
        while (current.parent != null) {
            path.add(0, new Point(current.row, current.col));
            current = current.parent;
        }
        return path;
    }

    // Manhattan distance heuristic.
    private float heuristic(Point a, Point b) {
        return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
    }

    // Internal Node class used by the A* algorithm.
    private static class Node {
        int row, col;
        Node parent;
        float g, h, f;

        Node(int row, int col, Node parent, float g, float h) {
            this.row = row;
            this.col = col;
            this.parent = parent;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }
}

// A simple helper class representing a grid cell.
class Point {
    public int row;
    public int col;

    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }
}
