package EngineRun;

import Level.*;
import RenderEngine.*;
import guis.GuiRenderer;
import org.lwjgl.opengl.Display;

public class MainGameLoop {

    private static void CleanUp
            (MasterRenderer renderer, GuiRenderer guiRenderer, Loader loader){

        renderer.CleanUp();
        guiRenderer.CleanUp();
        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }

    public static void main(String[] args){
        //ESSENTIALS
        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        MasterRenderer renderer = new MasterRenderer(loader);
        GuiRenderer guiRenderer = new GuiRenderer(loader);

        //Level
        Level testLevel = new ForestLevel(loader, renderer, guiRenderer);
        testLevel.LoadLevel();

        //FINISHED LOADING
        System.out.println("Finished Loading...");

        while (!Display.isCloseRequested()){

            testLevel.RenderLevel();
            testLevel.EventTick();

            //UPDATE FRAME
            DisplayManager.UpdateDisplay();
        }

        CleanUp(renderer, guiRenderer, loader);
    }
}
