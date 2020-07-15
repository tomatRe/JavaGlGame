package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;
    private final float cameraSpeed = 0.02f;
    private final float rotationSpeed = 0.5f;

    public Camera() {
    }

    public void Move(){
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            position.z -=  cameraSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            position.x +=  cameraSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            position.x -=  cameraSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            position.z +=  cameraSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            position.y +=  cameraSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
            position.y -=  cameraSpeed;
    }

    public void Rotate(){
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            pitch -=  rotationSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            pitch +=  rotationSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            yaw -=  rotationSpeed;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            yaw +=  rotationSpeed;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
