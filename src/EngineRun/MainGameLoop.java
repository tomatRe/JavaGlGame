package EngineRun;

import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.RawModel;
import RenderEngine.Renderer;
import org.lwjgl.opengl.Display;

public class MainGameLoop {

    public static void main(String[] args){

        //Square model
        float[] vertices = {
                -0.5f, 0.5f, 0f, //v0
                -0.5f, -0.5f, 0f, //v1
                0.5f, -0.5f, 0f, //v2
                0.5f, 0.5f, 0f, //v3
        };

        int[] indices ={
                0,1,3,
                3,1,2
        };


        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        RawModel model = loader.LoadtoVAO(vertices, indices);

        while (!Display.isCloseRequested()){
            renderer.Prepare();
            renderer.Render(model);
            DisplayManager.UpdateDisplay();
        }

        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
