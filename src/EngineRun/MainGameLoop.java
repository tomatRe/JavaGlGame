package EngineRun;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
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

    static final Vector3f lightColor = new Vector3f(1,1,1);
    static final Vector3f lightPosition = new Vector3f(0,50,0);

    public static void main(String[] args){

        //ESENTIALS
        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        StaticShader shader = new StaticShader();
        Renderer renderer = new Renderer(shader);
        Camera camera = new Camera();
        Light light = new Light(lightPosition, lightColor);

        //MODELS
        RawModel model = ObjLoader.LoadObjModel("fruit", loader);
        RawModel hiresmodel = ObjLoader.LoadObjModel("fruit_hipoly", loader);
        RawModel stallmodel = ObjLoader.LoadObjModel("stall", loader);
        RawModel dragonmodel = ObjLoader.LoadObjModel("dragon", loader);

        //TEXTURES
        ModelTexture texture = new ModelTexture(loader.LoadTexture("metal"));
        TextureModel textureModel = new TextureModel(model,texture);
        TextureModel hiresFruit = new TextureModel(hiresmodel);
        TextureModel stall = new TextureModel(stallmodel);
        TextureModel dragonText = new TextureModel(dragonmodel);

        Entity fruit = new Entity(textureModel,
                new Vector3f(0,-2,-5),
                new Vector3f(0,0,0), 0.5f);

        Entity fruit2 = new Entity(hiresFruit,
                new Vector3f(5,-2,-5),
                new Vector3f(0,0,0), 0.5f);

        Entity stallEntity = new Entity(stall,
                new Vector3f(0,0,0),
                new Vector3f(0,0,0), 0.5f);

        Entity dragon = new Entity(dragonText,
                new Vector3f(5,0,5),
                new Vector3f(0,0,0), 0.5f);

        System.out.println("Finished Loading");

        while (!Display.isCloseRequested()){
            //CAMERA
            camera.Move();
            camera.Rotate();

            //RENDERER
            renderer.Prepare();
            shader.Start();
            shader.LoadViewMatrix(camera);
            shader.LoadLight(light);

            //SCENE
            renderer.Render(fruit, shader);
            renderer.Render(fruit2, shader);
            renderer.Render(stallEntity, shader);
            renderer.Render(dragon, shader);

            //UPDATE FRAME
            shader.Stop();
            DisplayManager.UpdateDisplay();
        }

        shader.CleanUp();
        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
