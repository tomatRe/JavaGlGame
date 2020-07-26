package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;
    private final float cameraSpeed = 10f;
    private final float rotationSpeed = 100f;

    public Camera() {
    }

    public void Move(float deltaTime){
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            position.z -=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            position.x +=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_A))
            position.x -=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_S))
            position.z +=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            position.y +=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
            position.y -=  cameraSpeed*deltaTime;
    }

    public void Rotate(float deltaTime){
        if (Keyboard.isKeyDown(Keyboard.KEY_UP))
            pitch -=  rotationSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
            pitch +=  rotationSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
            yaw -=  rotationSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
            yaw +=  rotationSpeed*deltaTime;
    }

    public void SetRotation(Vector3f rotation){
        pitch = rotation.x;
        yaw = rotation.y;
        roll = rotation.z;
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
