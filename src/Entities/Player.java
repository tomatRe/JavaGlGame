package Entities;

import Models.TexturedModel;
import RenderEngine.DisplayManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

public class Player extends Entity {

    private static float deltaTime = DisplayManager.GetFrameTimeSeconds();
    private static final float MOVE_SPEED = 20;
    private static final float RUN_SPEED = 40;
    private static  final float TURN_SPEED = 160;
    private float currentSpeed = 0;
    private float currentTurnSpeed = 0;
    private Vector3f forwardVector;
    private Vector3f moveDirection = new Vector3f();

    public Player(TexturedModel model, Vector3f position, Vector3f rotation, float scale) {
        super(model, position, rotation, scale);
        //forwardVector = new Vector3f()
    }

    private void CheckInputs(){

        if (Keyboard.isKeyDown(Keyboard.KEY_W))
            moveDirection.x = MOVE_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_W) && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT))
            moveDirection.x = RUN_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_S))
            moveDirection.x = -MOVE_SPEED * deltaTime;
        else
            moveDirection.x = 0;

        /*
        if (Keyboard.isKeyDown(Keyboard.KEY_D))
            moveDirection.z =  MOVE_SPEED * deltaTime;
        else if (Keyboard.isKeyDown(Keyboard.KEY_A))
            moveDirection.z =  -MOVE_SPEED * deltaTime;
        else
            moveDirection.z = 0;*/

        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            Jump();
        else if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL))
            Crouch();
    }

    public void Move(){
        deltaTime = DisplayManager.GetFrameTimeSeconds();
        Vector3f currentRotation = super.getRotation();
        CheckInputs();
        float dx = (float) (moveDirection.x * Math.sin(Math.toRadians(currentRotation.y)));
        float dz = (float) (moveDirection.x * Math.cos(Math.toRadians(currentRotation.y)));
        
        super.IncreasePosition(dx, 0, dz);
    }

    private void Jump(){
        //TODO
    }

    private void Crouch(){
        //TODO
    }
}
