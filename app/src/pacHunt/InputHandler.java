package pacHunt;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {
    private Window window;
    private Camera camera;

    // Pro uchování předchozí pozice myši
    private double lastMouseX, lastMouseY;
    private boolean firstMouse = true;
    // Citlivost myši – upravte podle preferencí
    private float mouseSensitivity = 0.1f;

    // Konstruktor nyní přijímá také instanci kamery, kterou upravujeme při pohybu myší
    public InputHandler(Window window, Camera camera) {
        this.window = window;
        this.camera = camera;

        // Skryjeme kurzor a zvolíme režim zakladání kurzoru
        glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // Nastavení callbacku pro zpracování pohybu myši
        glfwSetCursorPosCallback(window.getWindowHandle(), (win, xpos, ypos) -> {
            if (firstMouse) {
                lastMouseX = xpos;
                lastMouseY = ypos;
                firstMouse = false;
            }

            double offsetX = xpos - lastMouseX;
            double offsetY = ypos - lastMouseY;
            lastMouseX = xpos;
            lastMouseY = ypos;

            // Aktualizace horizontálního (yaw) a vertikálního (pitch) úhlu kamery
            camera.addYaw((float) offsetX * mouseSensitivity);
            camera.addPitch((float) -offsetY * mouseSensitivity);
        });
    }

    // Metoda, která se volá v herní smyčce a zpracovává vstupy z klávesnice
    public void processInput(float deltaTime) {
        float moveSpeed = 3.0f;
        long winHandle = window.getWindowHandle();

        // Pohyb vpřed a vzad
        if (glfwGetKey(winHandle, GLFW_KEY_W) == GLFW_PRESS) {
            camera.moveForward(moveSpeed * deltaTime);
        }
        if (glfwGetKey(winHandle, GLFW_KEY_S) == GLFW_PRESS) {
            camera.moveBackward(moveSpeed * deltaTime);
        }
        // Pohyb do stran (strafe vlevo a vpravo)
        if (glfwGetKey(winHandle, GLFW_KEY_A) == GLFW_PRESS) {
            camera.moveLeft(moveSpeed * deltaTime);
        }
        if (glfwGetKey(winHandle, GLFW_KEY_D) == GLFW_PRESS) {
            camera.moveRight(moveSpeed * deltaTime);
        }
    }
}
