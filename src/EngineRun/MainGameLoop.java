package EngineRun;

import Entities.Camera;
import Entities.Entity;
import Models.TextureModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import Models.RawModel;
import RenderEngine.Renderer;
import Shaders.StaticShader;
import Textures.ModelTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

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
        //TEXTURE COORDS
        float[] textureCoords = {
            0,0,//v0
            0,1,//v1
            1,1,//v2
            1,0 //v3
        };

        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);

        RawModel model = loader.LoadtoVAO(vertices,textureCoords, indices);
        ModelTexture texture = new ModelTexture(loader.LoadTexture("metal"));
        TextureModel textureModel = new TextureModel(model,texture);

        Entity entity = new Entity
                (textureModel, new Vector3f(0,0,-1), new Vector3f(0,0,0), 1);
        Camera camera = new Camera();

        while (!Display.isCloseRequested()){
            entity.IncreasePosition(0,0,0);
            camera.Move();
            camera.Rotate();

            renderer.Prepare();
            shader.Start();
            shader.LoadViewMatrix(camera);
            renderer.Render(entity, shader);
            shader.Stop();
            DisplayManager.UpdateDisplay();
        }

        shader.CleanUp();
        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
