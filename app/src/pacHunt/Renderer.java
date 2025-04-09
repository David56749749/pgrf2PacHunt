package pacHunt;

import pacHunt.Solids.Cube;

import static org.lwjgl.opengl.GL11.*;

public class Renderer {

    public void init(int width, int height) {
        glClearColor(0.1f, 0.1f, 0.1f, 0.0f);
        glEnable(GL_DEPTH_TEST); // osetreni viditelnosti

        // Nastavení projekční matice (perspektivní projekce)
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        float aspect = (float) width / height;
        float fov = 60.0f;
        float near = 0.1f;
        float far = 100.0f;
        float top = (float) Math.tan(Math.toRadians(fov / 2)) * near;
        float bottom = -top;
        float right = top * aspect;
        float left = -right;
        glFrustum(left, right, bottom, top, near, far);
        glMatrixMode(GL_MODELVIEW);
    }

    public void prepareFrame() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glLoadIdentity();
    }

    // Nastavení kamery pomocí vlastní implementace gluLookAt
    public void setCamera(Camera camera) {
        float eyeX = camera.getX();
        float eyeY = camera.getY();
        float eyeZ = camera.getZ();
        float angle = camera.getYaw();
        float lookX = eyeX + (float) Math.sin(Math.toRadians(angle));
        float lookY = eyeY;
        float lookZ = eyeZ - (float) Math.cos(Math.toRadians(angle));
        gluLookAt(eyeX, eyeY, eyeZ, lookX, lookY, lookZ, 0.0f, 1.0f, 0.0f);
    }

    // Vlastní implementace gluLookAt
    private void gluLookAt(float eyeX, float eyeY, float eyeZ,
                           float centerX, float centerY, float centerZ,
                           float upX, float upY, float upZ) {
        float[] forward = { centerX - eyeX, centerY - eyeY, centerZ - eyeZ };
        float fLength = (float) Math.sqrt(forward[0] * forward[0] +
                forward[1] * forward[1] +
                forward[2] * forward[2]);
        forward[0] /= fLength; forward[1] /= fLength; forward[2] /= fLength;

        float upLength = (float) Math.sqrt(upX * upX + upY * upY + upZ * upZ);
        float[] up = { upX / upLength, upY / upLength, upZ / upLength };

        float[] side = {
                forward[1] * up[2] - forward[2] * up[1],
                forward[2] * up[0] - forward[0] * up[2],
                forward[0] * up[1] - forward[1] * up[0]
        };

        // Upravení up vektoru
        up[0] = side[1] * forward[2] - side[2] * forward[1];
        up[1] = side[2] * forward[0] - side[0] * forward[2];
        up[2] = side[0] * forward[1] - side[1] * forward[0];

        float[] m = new float[16];
        m[0]  = side[0];  m[4]  = side[1];  m[8]  = side[2];  m[12] = 0.0f;
        m[1]  = up[0];    m[5]  = up[1];    m[9]  = up[2];    m[13] = 0.0f;
        m[2]  = -forward[0]; m[6] = -forward[1]; m[10] = -forward[2]; m[14] = 0.0f;
        m[3] = m[7] = m[11] = 0.0f;
        m[15] = 1.0f;
        glMultMatrixf(m);
        glTranslatef(-eyeX, -eyeY, -eyeZ);
    }

    // Metoda pro vykreslení kostky
    // V Renderer.java
    public void drawCube(float x, float y, float z, float size) {
        Cube cube = new Cube(x, y, z, size);
        cube.draw();
    }
}
