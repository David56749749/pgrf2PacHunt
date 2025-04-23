package game;

import static org.lwjgl.opengl.GL11.*;
import java.util.ArrayList;
import java.util.List;
import Solids.Obstacle;  // Import the Obstacle class

public class Maze {
    // 2D array representing the maze (1 = wall, 0 = free cell)
    private int[][] maze = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,1,1,0,0,1,0,0,1},
            {1,0,1,0,0,0,0,0,0,1},
            {1,0,1,0,1,1,1,1,0,1},
            {1,0,1,0,1,0,0,0,0,1},
            {1,0,0,0,1,0,1,1,0,1},
            {1,0,1,0,0,0,1,0,0,1},
            {1,0,1,1,1,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1}
    };

    private float tileSize = 1.0f;
    // Define a constant for the actual floor level (the floor mesh is drawn at y = 0)
    private final float FREE_FLOOR = 0.0f;
    // Define the player’s height offset above the floor (used for safe spawning and collision clamping)
    private final float PLAYER_HEIGHT = 1.0f;

    // List of obstacles randomly placed in free cells
    private List<Obstacle> obstacles;

    // Constructor: populate free cells with obstacles at random.
    public Maze() {
        obstacles = new ArrayList<>();
        int rows = maze.length;
        int cols = maze[0].length;
        // Define an obstacle size slightly smaller than a tile.
        float obstacleSize = tileSize * 0.8f;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col] == 0) {
                    // With a 20% probability, add an obstacle.
                    if (Math.random() < 0.2) {
                        // Calculate the bottom-left world coordinates for the cell.
                        float cellX = (col - cols / 2) * tileSize;
                        float cellZ = (row - rows / 2) * tileSize;
                        // Center the obstacle horizontally within the cell.
                        float obstacleX = cellX + (tileSize - obstacleSize) / 2;
                        float obstacleZ = cellZ + (tileSize - obstacleSize) / 2;
                        // Place the obstacle on the free floor (y = FREE_FLOOR)
                        obstacles.add(new Obstacle(obstacleX, FREE_FLOOR, obstacleZ, obstacleSize, 0));
                    }
                }
            }
        }
    }

    public void draw(Renderer renderer) {
        int rows = maze.length;
        int cols = maze[0].length;
        // Draw walls.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col] == 1) {
                    // Alternate wall colors.
                    if ((row + col) % 2 == 0) {
                        glColor3f(0.8f, 0.3f, 0.3f);
                    } else {
                        glColor3f(0.3f, 0.8f, 0.3f);
                    }
                    float x = (col - cols / 2) * tileSize;
                    float z = (row - rows / 2) * tileSize;
                    // Draw two cubes for a wall (first from FREE_FLOOR to FREE_FLOOR+tileSize, then above it)
                    renderer.drawCube(x, FREE_FLOOR, z, tileSize);
                    renderer.drawCube(x, FREE_FLOOR + tileSize, z, tileSize);
                }
            }
        }
        // (Optional) Draw a floor for free cells here if desired.

        // Draw the obstacles.
        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                obstacle.draw();
            }
        }
    }

    // Collision detection for horizontal movement.
    // Walls block regardless of vertical position.
    // Obstacles are checked only when the player is not high enough.
    public boolean collides(float camX, float camY, float camZ, float radius) {
        int rows = maze.length;
        int cols = maze[0].length;
        // Check wall collisions.
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col] == 1) {
                    float cellX = (col - cols / 2) * tileSize;
                    float cellZ = (row - rows / 2) * tileSize;
                    if (circleIntersectsAABB(camX, camZ, radius, cellX, cellZ, tileSize, tileSize)) {
                        return true;
                    }
                }
            }
        }
        // Check obstacle collisions.
        float clearance = 0.2f;
        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                // Only check collision if the camera's height is below the obstacle top plus clearance.
                if (camY <= (obstacle.getY() + obstacle.getSize() + clearance)) {
                    if (circleIntersectsAABB(camX, camZ, radius, obstacle.getX(), obstacle.getZ(), obstacle.getSize(), obstacle.getSize())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Helper: circle-AABB intersection on the XZ plane.
    private boolean circleIntersectsAABB(float cx, float cz, float radius, float ax, float az, float aw, float ah) {
        float closestX = clamp(cx, ax, ax + aw);
        float closestZ = clamp(cz, az, az + ah);
        float dx = cx - closestX;
        float dz = cz - closestZ;
        return (dx * dx + dz * dz) < (radius * radius);
    }

    private float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

    // Returns a safe spawn position: center of a free cell with camera offset.
    public float[] getSafeSpawnPosition() {
        List<float[]> safeCells = new ArrayList<>();
        int rows = maze.length;
        int cols = maze[0].length;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (maze[row][col] == 0) {
                    float x = (col - cols / 2) * tileSize;
                    float z = (row - rows / 2) * tileSize;
                    safeCells.add(new float[]{x, z});
                }
            }
        }
        if (!safeCells.isEmpty()) {
            int index = (int)(Math.random() * safeCells.size());
            float[] chosen = safeCells.get(index);
            // Place camera at free floor plus player height.
            return new float[]{chosen[0] + tileSize * 0.5f, FREE_FLOOR + PLAYER_HEIGHT, chosen[1] + tileSize * 0.5f};
        }
        return new float[]{0.0f, FREE_FLOOR + PLAYER_HEIGHT, 0.0f};
    }

    // Returns the platform height at (camX, camZ).
    // For free cells, this returns FREE_FLOOR + PLAYER_HEIGHT.
    // For obstacles, if the position lies within an obstacle, it returns the obstacle's top.
    public float getPlatformHeightAt(float camX, float camZ) {
        // For a free cell, the correct camera position is floor level plus the player's eye offset.
        float baseFloor = FREE_FLOOR;
        float desiredHeight = baseFloor + PLAYER_HEIGHT;
        float playerRadius = 0.2f;
        if (obstacles != null) {
            for (Obstacle obstacle : obstacles) {
                if (circleIntersectsAABB(camX, camZ, playerRadius,
                        obstacle.getX(), obstacle.getZ(),
                        obstacle.getSize(), obstacle.getSize())) {
                    // When on an obstacle, set the desired camera height to obstacle top plus the PLAYER_HEIGHT offset.
                    float obstacleTop = obstacle.getY() + obstacle.getSize();
                    desiredHeight = Math.max(desiredHeight, obstacleTop + PLAYER_HEIGHT);
                }
            }
        }
        return desiredHeight;
    }
    public int[][] getMazeGrid(){
        return this.maze;
    }
}
