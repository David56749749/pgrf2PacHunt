package pacHunt;

import static org.lwjgl.glfw.GLFW.*;

public class Game {
    private Window window;
    private Renderer renderer;
    private Maze maze;
    private InputHandler inputHandler;
    private Camera camera;

    public Game() {
        window = new Window("PacHunt", 1800, 700);
        renderer = new Renderer();
        maze = new Maze();
        InputHandler inputHandler = new InputHandler(window, camera);
        camera = new Camera(); // počáteční pozice hráče a úhel jsou nastaveny ve třídě Camera
    }

    public void run() {
        window.create();
        renderer.init(window.getWidth(), window.getHeight());
        loop();
        window.destroy();
    }

    private void loop() {
        float lastTime = (float) System.nanoTime() / 1_000_000_000.0f;
        while (!window.shouldClose()) {
            float currentTime = (float) System.nanoTime() / 1_000_000_000.0f;
            float deltaTime = currentTime - lastTime;
            lastTime = currentTime;

            glfwPollEvents();
            inputHandler.processInput(deltaTime);
            renderer.prepareFrame();
            renderer.setCamera(camera);
            maze.draw(renderer);
            window.swapBuffers();
        }
    }
}
