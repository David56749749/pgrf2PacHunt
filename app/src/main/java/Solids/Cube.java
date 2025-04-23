package Solids;

import static org.lwjgl.opengl.GL11.*;

public class Cube implements Solid {
    private float x, y, z;
    private float size;
    private int textureID; // Identifikátor textury načtené pomocí OpenGL

    public Cube(float x, float y, float z, float size, int textureID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
        this.textureID = textureID;
    }

    public void draw() {
        float halfSize = size / 2.0f;

        // Enable texturing and bind the texture
        glEnable(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, textureID);

        glPushMatrix();
        // Move cube so that its center is at (x+halfSize, y+halfSize, z+halfSize)
        glTranslatef(x + halfSize, y + halfSize, z + halfSize);

        glBegin(GL_QUADS);
        // Front Face – (unchanged)
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-halfSize, -halfSize,  halfSize);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize, -halfSize,  halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize,  halfSize,  halfSize);

        // Back Face – flip U coordinate so texture isn’t mirrored horizontally
        glTexCoord2f(1.0f, 0.0f); glVertex3f(-halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f( halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f( halfSize, -halfSize, -halfSize);

        // Top Face – adjust so that the texture is correctly oriented
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize,  halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-halfSize,  halfSize,  halfSize);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f( halfSize,  halfSize, -halfSize);

        // Bottom Face – adjust U similarly to prevent mirror effect
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( halfSize, -halfSize,  halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize, -halfSize,  halfSize);

        // Right Face – flip U coordinate for proper orientation
        glTexCoord2f(1.0f, 0.0f); glVertex3f( halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f( halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f( halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f( halfSize, -halfSize,  halfSize);

        // Left Face – flip U coordinate for proper orientation
        glTexCoord2f(1.0f, 0.0f); glVertex3f(-halfSize, -halfSize, -halfSize);
        glTexCoord2f(1.0f, 1.0f); glVertex3f(-halfSize,  halfSize, -halfSize);
        glTexCoord2f(0.0f, 1.0f); glVertex3f(-halfSize,  halfSize,  halfSize);
        glTexCoord2f(0.0f, 0.0f); glVertex3f(-halfSize, -halfSize,  halfSize);
        glEnd();

        glPopMatrix();
        glDisable(GL_TEXTURE_2D);
    }
}
