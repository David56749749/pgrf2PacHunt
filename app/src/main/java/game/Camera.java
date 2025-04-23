package game;

public class Camera {
    private float x = 2.4f;
    private float y = 1.0f;
    private float z = 2.4f;
    private float yaw = 0.0f;
    private float pitch = 0.0f;

    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }

    public void moveForward(float distance) {
        x += distance * (float) Math.sin(Math.toRadians(yaw));
        z -= distance * (float) Math.cos(Math.toRadians(yaw));
    }

    public void moveBackward(float distance) {
        x -= distance * (float) Math.sin(Math.toRadians(yaw));
        z += distance * (float) Math.cos(Math.toRadians(yaw));
    }

    public void moveLeft(float distance) {
        x += distance * (float) Math.sin(Math.toRadians(yaw - 90));
        z -= distance * (float) Math.cos(Math.toRadians(yaw - 90));
    }

    public void moveRight(float distance) {
        x += distance * (float) Math.sin(Math.toRadians(yaw + 90));
        z -= distance * (float) Math.cos(Math.toRadians(yaw + 90));
    }

    public void addYaw(float angleDelta) {
        yaw += angleDelta;
    }

    public void addPitch(float pitchDelta) {
        pitch += pitchDelta;
        if (pitch > 89.0f) {
            pitch = 89.0f;
        } else if (pitch < -89.0f) {
            pitch = -89.0f;
        }
    }

    // New setter method for collision-based updates
    public void setPosition(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}
