package pacHunt.Solids;

import static org.lwjgl.opengl.GL11.*;

public class Cube implements Solid {
    // Pozice krychle (levý dolní roh před posunutím pro centrování)
    private float x;
    private float y;
    private float z;
    // Velikost krychle (délka hrany)
    private float size;

    public Cube(float x, float y, float z, float size) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.size = size;
    }

    // Metoda vykreslí krychli
    public void draw() {
        float halfSize = size / 2.0f;
        // Uloží aktuální matici a posune počáteční bod krychle do jejího středu
        glPushMatrix();
        glTranslatef(x + halfSize, y + halfSize, z + halfSize);

        glBegin(GL_QUADS);
        // Front Face
        glVertex3f(-halfSize, -halfSize,  halfSize);
        glVertex3f( halfSize, -halfSize,  halfSize);
        glVertex3f( halfSize,  halfSize,  halfSize);
        glVertex3f(-halfSize,  halfSize,  halfSize);

        // Back Face
        glVertex3f(-halfSize, -halfSize, -halfSize);
        glVertex3f(-halfSize,  halfSize, -halfSize);
        glVertex3f( halfSize,  halfSize, -halfSize);
        glVertex3f( halfSize, -halfSize, -halfSize);

        // Top Face
        glVertex3f(-halfSize,  halfSize, -halfSize);
        glVertex3f(-halfSize,  halfSize,  halfSize);
        glVertex3f( halfSize,  halfSize,  halfSize);
        glVertex3f( halfSize,  halfSize, -halfSize);

        // Bottom Face
        glVertex3f(-halfSize, -halfSize, -halfSize);
        glVertex3f( halfSize, -halfSize, -halfSize);
        glVertex3f( halfSize, -halfSize,  halfSize);
        glVertex3f(-halfSize, -halfSize,  halfSize);

        // Right Face
        glVertex3f( halfSize, -halfSize, -halfSize);
        glVertex3f( halfSize,  halfSize, -halfSize);
        glVertex3f( halfSize,  halfSize,  halfSize);
        glVertex3f( halfSize, -halfSize,  halfSize);

        // Left Face
        glVertex3f(-halfSize, -halfSize, -halfSize);
        glVertex3f(-halfSize, -halfSize,  halfSize);
        glVertex3f(-halfSize,  halfSize,  halfSize);
        glVertex3f(-halfSize,  halfSize, -halfSize);
        glEnd();

        glPopMatrix();
    }

    // Případné další metody pro transformace, změnu pozice či velikosti...
}
