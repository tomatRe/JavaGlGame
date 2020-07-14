package EngineRun;

import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.RawModel;
import RenderEngine.Renderer;
import Shaders.ShaderProgram;
import Shaders.StaticShader;
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
        //INDICES
        int[] indices ={
                0,1,3,
                3,1,2
        };

        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();
        RawModel model = loader.LoadtoVAO(vertices, indices);

        while (!Display.isCloseRequested()){
            renderer.Prepare();
            shader.Start();
            renderer.Render(model);
            shader.Stop();
            DisplayManager.UpdateDisplay();
        }

        shader.CleanUp();
        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
