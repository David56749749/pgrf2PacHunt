package game;

import Solids.Pacman;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.opengl.GL.createCapabilities;

public class Game {
    private Window window;
    private Renderer renderer;
    private Maze maze;
    private InputHandler inputHandler;
    private Camera camera;
    private PacmanController pacmanController;
    private Pacman pacman;

    public Game() {
        window = new Window("PacHunt", 1800, 700);
        renderer = new Renderer();
        maze = new Maze();
        camera = new Camera();

        float[] safeSpawn = maze.getSafeSpawnPosition();
        camera.setPosition(safeSpawn[0], safeSpawn[1], safeSpawn[2]);
    }

    public void run() {
        window.create();

        // Make the GLFW context current and initialize LWJGL’s OpenGL bindings
        glfwMakeContextCurrent(window.getWindowHandle());
        createCapabilities();

        inputHandler = new InputHandler(window, camera, maze);
        renderer.init(window.getWidth(), window.getHeight());

        // Create Pac-Man once textures are ready
        float[] pacmanSpawn = maze.getSafeSpawnPosition();
        int pacTexture = renderer.getPacManTextureID();
        pacman = new Pacman(pacmanSpawn[0], pacmanSpawn[1], pacmanSpawn[2], 1.0f, pacTexture);
        pacmanController = new PacmanController(pacman, maze);

        loop();
        window.destroy();

        // Clean up all GPU textures
        TextureLoader.cleanup();
    }

    private void loop() {
        float lastTime = (float) System.nanoTime() / 1_000_000_000.0f;
        while (!window.shouldClose()) {
            float currentTime = (float) System.nanoTime() / 1_000_000_000.0f;
            float deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            glfwPollEvents();
            inputHandler.processInput(deltaTime);
            pacmanController.update(deltaTime, camera.getX(), camera.getZ());
            renderer.prepareFrame();
            renderer.setCamera(camera);
            maze.draw(renderer);
            pacman.draw(renderer);
            window.swapBuffers();
        }
    }
}
