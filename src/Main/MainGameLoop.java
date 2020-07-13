package Main;

import Engine.DisplayManager;
import org.lwjgl.opengl.Display;

public class MainGameLoop {

    public static void main(String[] args){

        DisplayManager.CreateDisplay();

        while (!Display.isCloseRequested()){
            DisplayManager.UpdateDisplay();
        }
        DisplayManager.CloseDisplay();
    }
}
