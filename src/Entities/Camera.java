package Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private boolean hasPlayer;

    private float distanceFromPlayer = 50;
    private float angleArroundPlayer = 0;

    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;
    private final float cameraSpeed = 60f;
    private final float rotationSpeed = 100f;

    public Player player;

    public Camera(){
        hasPlayer = false;
    }

    public Camera(Player player) {
        this.player = player;
        hasPlayer = true;
    }

    public void Move(float deltaTime){
        //ESSENTIALS TO FOLLOW PLAYER
        CalculateZoom();
        CalculatePitch();
        CalculateAngleArroundPlayer();
        float horizontalDistance = CalculateHorizontalDistance();
        float verticalDistance = CalculateVerticalDistance();
        CalculateCameraPosition(horizontalDistance, verticalDistance);
        yaw = 180-player.getRotation().y;

        //FREE CAMERA MOVEMENT
        if (!hasPlayer){
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
        float zoomLevel = Mouse.getDWheel() *0.1f;
        distanceFromPlayer -= zoomLevel;
    }

    private void CalculatePitch(){
        float pitchChange = Mouse.getDY() * 0.1f;
        pitch -= pitchChange;
    }

    private void CalculateAngleArroundPlayer(){
        float angleChange = Mouse.getDX() * 0.3f;
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
        position.y = player.getPosition().y + verticalDistance;
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
}
