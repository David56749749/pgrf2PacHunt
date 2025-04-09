package pacHunt;

public class Camera {
    // Pozice kamery; y reprezentuje výšku "očí"
    private float x = 0.0f;
    private float y = 1.0f;
    private float z = 0.0f;

    // Úhly kamery: yaw pro horizontální rotaci a pitch pro vertikální rotaci
    private float yaw = 0.0f;    // směr pohledu v horizontální rovině (ve stupních)
    private float pitch = 0.0f;  // vertikální úhel pohledu (ve stupních)

    // Gettery pro pozici a úhly
    public float getX() { return x; }
    public float getY() { return y; }
    public float getZ() { return z; }
    public float getYaw() { return yaw; }
    public float getPitch() { return pitch; }

    // Pohyb vpřed na základě aktuálního yaw
    public void moveForward(float distance) {
        x += distance * (float) Math.sin(Math.toRadians(yaw));
        z -= distance * (float) Math.cos(Math.toRadians(yaw));
    }

    // Pohyb dozadu na základě aktuálního yaw
    public void moveBackward(float distance) {
        x -= distance * (float) Math.sin(Math.toRadians(yaw));
        z += distance * (float) Math.cos(Math.toRadians(yaw));
    }

    // Strafing vlevo – posun kolmo k směru vpřed, tj. yaw - 90°
    public void moveLeft(float distance) {
        x += distance * (float) Math.sin(Math.toRadians(yaw - 90));
        z -= distance * (float) Math.cos(Math.toRadians(yaw - 90));
    }

    // Strafing vpravo – posun kolmo k směru vpřed, tj. yaw + 90°
    public void moveRight(float distance) {
        x += distance * (float) Math.sin(Math.toRadians(yaw + 90));
        z -= distance * (float) Math.cos(Math.toRadians(yaw + 90));
    }

    // Upravit horizontální směr (yaw) – volá se při pohybu myši vodorovně
    public void addYaw(float angleDelta) {
        yaw += angleDelta;
        // Případně můžete omezit rozsah yaw, ale obvykle je yaw volný
    }

    // Upravit vertikální směr (pitch) – volá se při pohybu myši svisle
    public void addPitch(float pitchDelta) {
        pitch += pitchDelta;
        // Omezíme pitch, aby nedošlo k "přetočení" (obvykle mezi -89° a 89°)
        if (pitch > 89.0f) {
            pitch = 89.0f;
        } else if (pitch < -89.0f) {
            pitch = -89.0f;
        }
    }
}
