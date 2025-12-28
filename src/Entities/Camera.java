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
    public boolean canMove = true;

    public Camera(){
        hasPlayer = false;
        position.y = 100;
        yaw = 180;
    }

    public Camera(Player player) {
        this.player = player;
        hasPlayer = true;
    }

    // Copy constructor
    public Camera(Camera copyCamera) {
        this.hasPlayer = copyCamera.hasPlayer;
        this.position = copyCamera.position;
        this.distanceFromPlayer = copyCamera.distanceFromPlayer;
        this.angleArroundPlayer = copyCamera.angleArroundPlayer;
        this.pitch = copyCamera.pitch;
        this.yaw = copyCamera.yaw;
        this.roll = copyCamera.roll;
        this.player = copyCamera.player;
        this.mouseSensitivity = copyCamera.mouseSensitivity;

        canMove = false;
    }

    public void Move(float deltaTime){
        if (canMove) {
            //FREE CAMERA MOVEMENT
            if (!hasPlayer){
                Vector3f direction = new Vector3f();
                if (Keyboard.isKeyDown(Keyboard.KEY_W))
                    direction.x -=  cameraSpeed*deltaTime;
                if (Keyboard.isKeyDown(Keyboard.KEY_D))
                    direction.z -=  cameraSpeed*deltaTime;
                if (Keyboard.isKeyDown(Keyboard.KEY_A))
                    direction.z +=  cameraSpeed*deltaTime;
                if (Keyboard.isKeyDown(Keyboard.KEY_S))
                    direction.x +=  cameraSpeed*deltaTime;
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                    direction.y +=  cameraSpeed*deltaTime;
                if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
                    direction.y -=  cameraSpeed*deltaTime;
                if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
                    System.exit(0);

                moveFloatingCamera(direction);
                Rotate();

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
    }

    private void moveFloatingCamera(Vector3f moveDirection){
        float dx = 0;
        float dy = 0;
        float dz = 0;

        //DIAGONAL MOVEMENT
        if ((moveDirection.z > 0 || moveDirection.z < 0) &&
                (moveDirection.x < 0 || moveDirection.x > 0)){

            dx = moveDirection.x * (float) Math.sin(
                    Math.toRadians(getYaw()));
            dz = moveDirection.x * (float) Math.cos(
                    Math.toRadians(getYaw()));


            dx += moveDirection.z/2 * (float) Math.sin(
                    Math.toRadians(getYaw() + 90));

            dz += moveDirection.z/2 * (float) Math.cos(
                    Math.toRadians(getYaw() + 90));
        }
        //FORWARD MOVEMENT
        else if (moveDirection.x > 0 || moveDirection.x < 0){
            dx = moveDirection.x * (float) Math.sin(
                    Math.toRadians(getYaw()));
            dz = moveDirection.x * (float) Math.cos(
                    Math.toRadians(getYaw()));
        }
        // L/R MOVEMENT
        else if (moveDirection.z < 0 || moveDirection.z > 0){
            dx += moveDirection.z * (float) Math.sin(
                    Math.toRadians(getYaw() + 90));

            dz += moveDirection.z * (float) Math.cos(
                    Math.toRadians(getYaw() + 90));
        }

        if (moveDirection.y < 0 || moveDirection.y > 0){
            dy += moveDirection.y;
        }

        position.x -= dx;
        position.y += dy;
        position.z += dz;
    }

    public void Rotate(){
        if (!hasPlayer){
            pitch -= Mouse.getDY() * 0.1f;
            yaw += Mouse.getDX() * 0.1f;
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

    public void setPosition(Vector3f position){
        this.position = position;
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
