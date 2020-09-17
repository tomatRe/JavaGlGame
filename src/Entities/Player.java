package Entities;

import Models.TexturedModel;
import RenderEngine.DisplayManager;
import Terrains.Terrain;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Display;
import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {

    private static float deltaTime;
    private static final float MOVE_SPEED = 20;
    private static final float RUN_SPEED = 100;
    private static final float GRAVITY = -2f;
    private static final float JUMP_POWER = 0.5f;
    private static final float PLAYER_HEIGHT = 1;
    private float currentTurnSpeed;
    private float upwardSpeed;
    private boolean isInAir;
    private Vector3f moveDirection;

    public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        
        deltaTime = DisplayManager.GetFrameTimeSeconds();
        currentTurnSpeed = 0;
        upwardSpeed = 0;
        isInAir = false;
        moveDirection = new Vector3f();
    }

    private void CheckInputs(){
        deltaTime = DisplayManager.GetFrameTimeSeconds();

        // FORWARDS / BACKWARDS
        if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            moveDirection.x = RUN_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_W))
            moveDirection.x = MOVE_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            moveDirection.x = -MOVE_SPEED * deltaTime;
        else
            moveDirection.x = 0;

        // ROTATE
        currentTurnSpeed = -Mouse.getDX() * 0.1f;

        // LEFT / RIGHT
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            moveDirection.z = -MOVE_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A))
            moveDirection.z = MOVE_SPEED * deltaTime;
        else
            moveDirection.z = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            Jump();
        else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
            Crouch();

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            System.exit(0);
    }

    public void Move(Terrain terrain){
        CheckInputs();

        float terraomHeight = terrain.getHeight(super.getPosition().x, super.getPosition().z);
        float dx = 0;
        float dz = 0;

        //DIAGONAL MOVEMENT
        if ((moveDirection.z > 0 || moveDirection.z < 0) &&
                (moveDirection.x < 0 || moveDirection.x > 0)){

            dx = moveDirection.x * (float) Math.sin(
                    Math.toRadians(super.getRotation().y));
            dz = moveDirection.x * (float) Math.cos(
                    Math.toRadians(super.getRotation().y));


            dx += moveDirection.z/2 * (float) Math.sin(
                    Math.toRadians(super.getRotation().y + 90));

            dz += moveDirection.z/2 * (float) Math.cos(
                    Math.toRadians(super.getRotation().y + 90));
        }
        //FORWARD MOVEMENT
        else if (moveDirection.x > 0 || moveDirection.x < 0){
            dx = moveDirection.x * (float) Math.sin(
                    Math.toRadians(super.getRotation().y));
            dz = moveDirection.x * (float) Math.cos(
                    Math.toRadians(super.getRotation().y));
        }
        // L/R MOVEMENT
        else if (moveDirection.z < 0 || moveDirection.z > 0){
            dx += moveDirection.z * (float) Math.sin(
                    Math.toRadians(super.getRotation().y + 90));

            dz += moveDirection.z * (float) Math.cos(
                    Math.toRadians(super.getRotation().y + 90));
        }

        //VERTICAL MOVEMENT (GRAVITY)
        upwardSpeed += GRAVITY * deltaTime;

        if (super.getPosition().y < terraomHeight + PLAYER_HEIGHT){
            upwardSpeed = 0;
            isInAir = false;
            super.getPosition().y = terraomHeight + PLAYER_HEIGHT;
        }

        //ADD CALCULATIONS TO THE PLAYER POSITION
        super.IncreaseRotation(0, currentTurnSpeed, 0);
        super.IncreasePosition(dx,upwardSpeed,dz);
    }

    private void Jump(){
        if (!isInAir){
            this.upwardSpeed = JUMP_POWER;
            isInAir = true;
        }

    }

    private void Crouch(){
        //TODO
    }
}
