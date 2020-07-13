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
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };

        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        RawModel model = loader.LoadtoVAO(vertices);

        while (!Display.isCloseRequested()){
            renderer.Prepare();
            renderer.Render(model);
            DisplayManager.UpdateDisplay();
        }

        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
