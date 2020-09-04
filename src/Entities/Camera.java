package Entities;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private boolean hasPlayer;

    private Vector3f position = new Vector3f(0,0,0);
    private float distanceFromPlayer = 30;
    private float angleArroundPlayer = 0;
    private float pitch;
    private float yaw;
    private float roll;

    private final float cameraSpeed = 60f;
    private final float rotationSpeed = 100f;
    private final float cameraHeight = 1;
    private static float minDistanceFromPlayer = 15;
    private static float maxDistanceFromPlayer = 100;

    public Player player;
    public float mouseSensitivity = 0.1f;

    public Camera(){
        hasPlayer = false;
    }

    public Camera(Player player) {
        this.player = player;
        hasPlayer = true;
    }

    public void Move(float deltaTime){
        //FREE CAMERA MOVEMENT
        if (!hasPlayer){
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
            Rotate(deltaTime);

        }else{ //FOLLOW PLAYER MOVEMENT
            CalculateZoom();
            CalculatePitch();
            //CalculateAngleArroundPlayer();
            float horizontalDistance = CalculateHorizontalDistance();
            float verticalDistance = CalculateVerticalDistance();
            CalculateCameraPosition(horizontalDistance, verticalDistance);
            yaw = 180-player.getRotation().y;
        }
    }

    public void Rotate(float deltaTime){
        if (!hasPlayer){
            if (Keyboard.isKeyDown(Keyboard.KEY_UP))
                pitch -=  rotationSpeed*deltaTime;
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
                pitch +=  rotationSpeed*deltaTime;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
                yaw -=  rotationSpeed*deltaTime;
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
                yaw +=  rotationSpeed*deltaTime;
        }
    }

    public void CalculateZoom(){
        if(distanceFromPlayer >= minDistanceFromPlayer && distanceFromPlayer <= maxDistanceFromPlayer){
            float zoomLevel = Mouse.getDWheel() * mouseSensitivity;
            distanceFromPlayer -= zoomLevel;
        }
        if (distanceFromPlayer < minDistanceFromPlayer){
            distanceFromPlayer = minDistanceFromPlayer;
        }
        if (distanceFromPlayer > maxDistanceFromPlayer){
            distanceFromPlayer = maxDistanceFromPlayer;
        }
    }

    private void CalculatePitch(){
        float pitchChange = Mouse.getDY() * mouseSensitivity;
        pitch -= pitchChange;

        if (pitch >= 85){
            pitch = 84.99f;
        }
        if (pitch<= 1)
            pitch = 1.01f;
    }

    private void CalculateAngleArroundPlayer(){
        float angleChange = Mouse.getDX() * mouseSensitivity;
        angleArroundPlayer += angleChange;
    }

    private float CalculateHorizontalDistance(){
        return (float) (distanceFromPlayer * Math.cos(Math.toRadians(pitch)));
    }

    private float CalculateVerticalDistance(){
        return (float) (distanceFromPlayer * Math.sin(Math.toRadians(pitch)));
    }

    private void CalculateCameraPosition(float horizontalDistance, float verticalDistance){
        float offsetX = (float)(horizontalDistance * Math.sin(Math.toRadians(player.getRotation().y)));
        float offsetZ = (float)(horizontalDistance * Math.cos(Math.toRadians(player.getRotation().y)));

        position.x = player.getPosition().x - offsetX;
        position.y = player.getPosition().y + verticalDistance + cameraHeight;
        position.z = player.getPosition().z - offsetZ;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
        hasPlayer = true;
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

    public float getMouseSensitivity() {
        return mouseSensitivity;
    }

    public void setMouseSensitivity(float mouseSensitivity) {
        this.mouseSensitivity = mouseSensitivity;
    }
}
