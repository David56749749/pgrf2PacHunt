package Solids;

import game.Renderer;

/**
 * Represents the Pac-Man character. Texture is supplied externally.
 */
public class Pacman {
    private float x, y, z;
    private float speed;
    private final int textureID;

    /**
     * Creates Pac-Man at the given coordinates with the given speed and texture.
     */
    public Pacman(float x, float y, float z, float speed, int textureID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.speed = speed;
        this.textureID = textureID;
    }

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getSpeed() { return speed; }

    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Draws Pac-Man as a textured sphere.
     */
    public void draw(Renderer renderer) {
        renderer.drawSphere(
                this.x - 0.25f,
                this.y,
                this.z - 0.25f,
                0.5f,
                16,
                32,
                this.textureID
        );
    }
}