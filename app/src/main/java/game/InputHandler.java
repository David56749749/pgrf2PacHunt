package game;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
    private Window window;
    private Camera camera;
    private Maze maze; // For collision checking and platform height

    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;
    private float mouseSensitivity = 1.4f;

    // Field to track vertical (jumping) velocity
    private float verticalVelocity = 0.0f;
    // Constant for jump height (initial jump velocity)
    private final float jumpHeight = 5.0f;

    // Flag to toggle reduced gravity mode and a flag to detect one-time key press events for G.
    private boolean gravityReduced = false;
    private boolean gPressedLastFrame = false;

    // Flag for one-time jump (space) detection.
    private boolean spacePressedLastFrame = false;

    public InputHandler(Window window, Camera camera, Maze maze) {
        this.window = window;
        this.camera = camera;
        this.maze = maze;

        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        glfwSetCursorPosCallback(window.getWindowHandle(), (win, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
            }

            double offsetX = xpos - lastMouseX;
            double offsetY = lastMouseY - ypos;

            double maxHorizontalMovement = 2.0;
            if (offsetX > maxHorizontalMovement) offsetX = maxHorizontalMovement;
            if (offsetX < -maxHorizontalMovement) offsetX = -maxHorizontalMovement;

            double maxVerticalMovement = 2.0;
            if (offsetY > maxVerticalMovement) offsetY = maxVerticalMovement;
            if (offsetY < -maxVerticalMovement) offsetY = -maxVerticalMovement;

            lastMouseX = xpos;
            lastMouseY = ypos;

            camera.addYaw((float) offsetX * mouseSensitivity);
            camera.addPitch((float) offsetY * mouseSensitivity);
        });
    }

    public void processInput(float deltaTime) {
        long winHandle = window.getWindowHandle();
        float moveSpeed = 3.0f;

        // --- Toggle Reduced Gravity with G key ---
        if (glfwGetKey(winHandle, GLFW_KEY_G) == GLFW_PRESS) {
            if (!gPressedLastFrame) {
                gravityReduced = !gravityReduced;
            }
            gPressedLastFrame = true;
        } else {
            gPressedLastFrame = false;
        }

        // --- Horizontal Movement ---
        float dx = 0.0f, dz = 0.0f;
        if (glfwGetKey(winHandle, GLFW_KEY_W) == GLFW_PRESS) {
            dx += moveSpeed * deltaTime * (float) Math.sin(Math.toRadians(camera.getYaw()));
            dz -= moveSpeed * deltaTime * (float) Math.cos(Math.toRadians(camera.getYaw()));
        }
        if (glfwGetKey(winHandle, GLFW_KEY_S) == GLFW_PRESS) {
            dx -= moveSpeed * deltaTime * (float) Math.sin(Math.toRadians(camera.getYaw()));
            dz += moveSpeed * deltaTime * (float) Math.cos(Math.toRadians(camera.getYaw()));
        }
        if (glfwGetKey(winHandle, GLFW_KEY_A) == GLFW_PRESS) {
            dx += moveSpeed * deltaTime * (float) Math.sin(Math.toRadians(camera.getYaw() - 90));
            dz -= moveSpeed * deltaTime * (float) Math.cos(Math.toRadians(camera.getYaw() - 90));
        }
        if (glfwGetKey(winHandle, GLFW_KEY_D) == GLFW_PRESS) {
            dx += moveSpeed * deltaTime * (float) Math.sin(Math.toRadians(camera.getYaw() + 90));
            dz -= moveSpeed * deltaTime * (float) Math.cos(Math.toRadians(camera.getYaw() + 90));
        }

        // Check collisions horizontally using current camera Y.
        float collisionRadius = 0.2f;
        float currentX = camera.getX();
        float currentZ = camera.getZ();

        float newX = currentX;
        float newZ = currentZ;
        if (!maze.collides(currentX + dx, camera.getY(), currentZ, collisionRadius)) {
            newX = currentX + dx;
        }
        if (!maze.collides(newX, camera.getY(), currentZ + dz, collisionRadius)) {
            newZ = currentZ + dz;
        }

        // --- Vertical Movement: Jumping and Gravity ---
        // Get current platform (ground) height at current and new positions.
        float currentPlatform = maze.getPlatformHeightAt(camera.getX(), camera.getZ());
        float platformHeight = maze.getPlatformHeightAt(newX, newZ);

        // Process jump input using space key.
        if (glfwGetKey(winHandle, GLFW_KEY_SPACE) == GLFW_PRESS) {
            if (!spacePressedLastFrame) {
                // Allow jump only if the camera is essentially on the ground.
                if (Math.abs(camera.getY() - currentPlatform) < 0.01f) {
                    verticalVelocity = jumpHeight;
                }
            }
            spacePressedLastFrame = true;
        } else {
            spacePressedLastFrame = false;
        }

        // Apply gravity.
        // Use reduced gravity if toggled, otherwise normal gravity.
        float gravity = gravityReduced ? 1.8f : 9.8f;
        verticalVelocity -= gravity * deltaTime;
        float newY = camera.getY() + verticalVelocity * deltaTime;

        // Clamp newY so the camera does not fall below the platform.
        if (newY < platformHeight) {
            newY = platformHeight;
            verticalVelocity = 0;
        }

        camera.setPosition(newX, newY, newZ);
    }
}
