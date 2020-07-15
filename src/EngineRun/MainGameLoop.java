package EngineRun;

import Entities.Camera;
import Entities.Entity;
import Models.TextureModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import Models.RawModel;
import RenderEngine.ObjLoader;
import RenderEngine.Renderer;
import Shaders.StaticShader;
import Textures.ModelTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

public class MainGameLoop {

    public static void main(String[] args){

        //ESENTIALS
        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);
        Camera camera = new Camera();

        //TEST MODEL
        RawModel model = ObjLoader.LoadObjModel("fruit", loader);
        //RawModel potModel = ObjLoader.LoadObjModel("plant", loader);

        ModelTexture texture = new ModelTexture(loader.LoadTexture("metal"));
        TextureModel textureModel = new TextureModel(model,texture);
        //TextureModel textureModelpot = new TextureModel(potModel, texture);

        Entity fruit = new Entity(textureModel,
                new Vector3f(0,-2,-5),
                new Vector3f(0,0,0), 0.5f);

        /*Entity pot = new Entity(textureModelpot,
                new Vector3f(3,0,-2),
                new Vector3f(0,0,0), 0.5f);*/

        System.out.println("Finished Loading");

        while (!Display.isCloseRequested()){
            camera.Move();
            camera.Rotate();

            renderer.Prepare();
            shader.Start();
            shader.LoadViewMatrix(camera);

            renderer.Render(fruit, shader);
            //renderer.Render(pot, shader);

            shader.Stop();
            DisplayManager.UpdateDisplay();
        }

        shader.CleanUp();
        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
