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

    private static float deltaTime = DisplayManager.GetFrameTimeSeconds();
    private static final float MOVE_SPEED = 20;
    private static final float RUN_SPEED = 100;
    private static final float GRAVITY = -2f;
    private static final float JUMP_POWER = 0.5f;
    private static final float PLAYER_HEIGHT = 1;

    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;
    private boolean isInAir = false;

    private Vector3f forwardVector;
    private Vector3f moveDirection = new Vector3f();

    public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
    }

    private void CheckInputs(){
        deltaTime = DisplayManager.GetFrameTimeSeconds();

        // FORWARDS / BACKWARDS
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            moveDirection.x = MOVE_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            moveDirection.x = RUN_SPEED * deltaTime;
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

        float dx = moveDirection.x * (float) Math.sin(
                Math.toRadians(super.getRotation().y));
        float dz = moveDirection.x * (float) Math.cos(
                Math.toRadians(super.getRotation().y));

        upwardSpeed += GRAVITY * deltaTime;

        super.IncreaseRotation(0, currentTurnSpeed, 0);
        super.IncreasePosition(dx,upwardSpeed,dz);

        float terraomHeight = terrain.getHeight(super.getPosition().x, super.getPosition().z);

        if (super.getPosition().y < terraomHeight + PLAYER_HEIGHT){
            upwardSpeed = 0;
            isInAir = false;
            super.getPosition().y = terraomHeight + PLAYER_HEIGHT;
        }
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
