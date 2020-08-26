package Entities;

import Models.TexturedModel;
import RenderEngine.DisplayManager;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Display;
import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {

    private static float deltaTime = DisplayManager.GetFrameTimeSeconds();
    private static final float MOVE_SPEED = 20;
    private static final float RUN_SPEED = 40;
    private static final float TURN_SPEED = 160;
    private static final float GRAVITY = -9.8f;
    private static final float JUMP_POWER = 1;

    //this is temporal
    private static final float TERRAIN_HEIGHT = 0;

    private float currentSpeed = 0;
    private float currentLateralSpeed = 0;
    private float currentTurnSpeed = 0;
    private float upwardSpeed = 0;
    private boolean isInAir = false;

    private Vector3f forwardVector;
    private Vector3f moveDirection = new Vector3f();

    public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        //forwardVector = new Vector3f()
    }

    private void CheckInputs(){
        deltaTime = DisplayManager.GetFrameTimeSeconds();

        // FORWARDS / BACKWARDS
        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            currentSpeed = MOVE_SPEED * deltaTime;
            //moveDirection.x = MOVE_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            currentSpeed = RUN_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            currentSpeed = -MOVE_SPEED * deltaTime;
        else
            currentSpeed = 0;

        // ROTATE
        currentTurnSpeed = -Mouse.getDX() * 0.1f;

        // LEFT / RIGHT
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            currentLateralSpeed = -TURN_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A))
            currentLateralSpeed = TURN_SPEED * deltaTime;
        else
            currentLateralSpeed = 0;

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            Jump();
        else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
            Crouch();

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
            DisplayManager.CloseDisplay();
    }

    public void Move(){
        CheckInputs();

        float dx = currentSpeed * (float) Math.sin(
                Math.toRadians(super.getRotation().y));
        float dz = currentSpeed * (float) Math.cos(
                Math.toRadians(super.getRotation().y));

        upwardSpeed += GRAVITY * deltaTime;

        super.IncreaseRotation(0, currentTurnSpeed, 0);
        super.IncreasePosition(dx,upwardSpeed,dz);

        if (super.getPosition().y < TERRAIN_HEIGHT){
            upwardSpeed = 0;
            isInAir = false;
            super.getPosition().y = TERRAIN_HEIGHT;
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
