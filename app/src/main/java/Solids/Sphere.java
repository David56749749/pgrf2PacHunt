package Solids;

import static org.lwjgl.opengl.GL11.*;

public class Sphere implements Solid {
    private float x, y, z;        // current center
    private float prevX, prevZ;    // previous position for computing direction
    private float rotationY = 0f;  // rotation angle around Y-axis in degrees
    private float radius;          // sphere radius
    private int slices, stacks;
    private int textureID;         // OpenGL texture ID

    /**
     * Constructs a textured sphere at (x,y,z).
     */
    public Sphere(float x, float y, float z, float radius, int slices, int stacks, int textureID) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.prevX = x;
        this.prevZ = z;
        this.radius = radius;
        this.slices = slices;
        this.stacks = stacks;
        this.textureID = textureID;
    }

    @Override
    public void draw() {
        boolean textured = textureID > 0;
        if (textured) {
            glEnable(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, textureID);
        } else {
            glDisable(GL_TEXTURE_2D);
        }

        // Compute movement direction and update rotationY
        float dx = x - prevX;
        float dz = z - prevZ;
        if (dx * dx + dz * dz > 1e-6f) {
            rotationY = (float) Math.toDegrees(Math.atan2(dx, dz));
            prevX = x;
            prevZ = z;
        }

        glPushMatrix();
        // Translate to sphere center, then rotate so texture faces movement direction
        glTranslatef(x, y, z);
        glRotatef(rotationY, 0f, 1f, 0f);

        // Draw the sphere via quad strips
        for (int i = 0; i < stacks; i++) {
            double lat0 = Math.PI * (-0.5 + (double) i / stacks);
            double z0   = Math.sin(lat0);
            double zr0  = Math.cos(lat0);

            double lat1 = Math.PI * (-0.5 + (double) (i + 1) / stacks);
            double z1   = Math.sin(lat1);
            double zr1  = Math.cos(lat1);

            glBegin(GL_QUAD_STRIP);
            for (int j = 0; j <= slices; j++) {
                double lng   = 2 * Math.PI * j / slices;
                double xUnit = Math.cos(lng);
                double yUnit = Math.sin(lng);

                // Original UV coords
                float uRaw   = (float) j / slices;
                float vRaw0  = (float) i / stacks;
                float vRaw1  = (float) (i + 1) / stacks;

                // Rotate texture by -90°: newU = vRaw, newV = 1 - uRaw
                float u0 = vRaw0;
                float v0 = 1.0f - uRaw;
                float u1 = vRaw1;
                float v1 = 1.0f - uRaw;

                if (textured) glTexCoord2f(u0, v0);
                glNormal3d(xUnit * zr0, yUnit * zr0, z0);
                glVertex3d(xUnit * zr0 * radius, yUnit * zr0 * radius, z0 * radius);

                if (textured) glTexCoord2f(u1, v1);
                glNormal3d(xUnit * zr1, yUnit * zr1, z1);
                glVertex3d(xUnit * zr1 * radius, yUnit * zr1 * radius, z1 * radius);
            }
            glEnd();
        }

        glPopMatrix();

        if (textured) {
            glDisable(GL_TEXTURE_2D);
        }
    }
}
