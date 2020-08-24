package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;
    private final float cameraSpeed = 60f;
    private final float rotationSpeed = 100f;

    public Camera() {
    }

    public void Move(float deltaTime){
        if (Keyboard.isKeyDown(Keyboard.KEY_I))
            position.z -=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_L))
            position.x +=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_J))
            position.x -=  cameraSpeed*deltaTime;
        if (Keyboard.isKeyDown(Keyboard.KEY_K))
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
