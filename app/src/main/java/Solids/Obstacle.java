package Solids;

import static org.lwjgl.opengl.GL11.*;

public class Obstacle implements Solid {
    private float x, y, z;
    private float size;
    private int textureID;

    public Obstacle(float x, float y, float z, float size, int textureID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.textureID = textureID;
    }

    @Override
    public void draw() {
        float halfSize = size / 2.0f;
        glPushMatrix();
        // Translate so that the cube’s center is in the middle of the obstacle
        glTranslatef(x + halfSize, y + halfSize, z + halfSize);
        // If no texture is provided, use a custom color
        if (textureID == 0) {
            glDisable(GL_TEXTURE_2D);
            glColor3f(0.8f, 0.8f, 0.0f); // yellowish color for obstacles
        } else {
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, textureID);
        }

        glBegin(GL_QUADS);
        // Front Face
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-halfSize, -halfSize,  halfSize);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize, -halfSize,  halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize,  halfSize,  halfSize);

        // Back Face
        glTexCoord2f(1.0f, 0.0f); glVertex3f(-halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f( halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f( halfSize, -halfSize, -halfSize);

        // Top Face
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize,  halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-halfSize,  halfSize,  halfSize);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f( halfSize,  halfSize, -halfSize);

        // Bottom Face
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( halfSize, -halfSize,  halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize, -halfSize,  halfSize);

        // Right Face
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f( halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f( halfSize, -halfSize,  halfSize);

        // Left Face
        glTexCoord2f(1.0f, 0.0f); glVertex3f(-halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-halfSize, -halfSize,  halfSize);
        glEnd();

        if (textureID == 0) {
            glEnable(GL_TEXTURE_2D);
            glColor3f(1f, 1f, 1f); // Reset to white color
        }
        glPopMatrix();
    }

    // Add getters for the obstacle properties.
    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getSize() {
        return size;
    }
}
