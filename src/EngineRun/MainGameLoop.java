package EngineRun;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Models.TexturedModel;
import RenderEngine.*;
import Models.RawModel;
import Shaders.StaticShader;
import Terrains.Terrain;
import Textures.ModelTexture;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainGameLoop {

    static final Vector3f lightColor = new Vector3f(1,1,1);
    static final Vector3f lightPosition = new Vector3f(0,50,0);
    static final boolean showFps = true;

    public static void main(String[] args){

        //FPS INFO
        int fps = 0;
        float timePerFrame = 0;
        float totalTime = 0;

        //ESENTIALS
        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        Camera camera = new Camera();
        Light light = new Light(lightPosition, lightColor);
        MasterRenderer renderer = new MasterRenderer();

        //MODELS
        RawModel model = ObjLoader.LoadObjModel("fruit", loader);
        RawModel hiresmodel = ObjLoader.LoadObjModel("fruit_hipoly", loader);
        RawModel stallmodel = ObjLoader.LoadObjModel("stall", loader);
        RawModel dragonmodel = ObjLoader.LoadObjModel("dragon", loader);

        //TEXTURES
        ModelTexture texture = new ModelTexture(loader.LoadTexture("metal"));
        texture.setShineDumper(10);
        texture.setReflectivity(1);

        TexturedModel texturedModel = new TexturedModel(model,texture);
        TexturedModel hiresFruit = new TexturedModel(hiresmodel);
        TexturedModel stall = new TexturedModel(stallmodel);
        TexturedModel dragonText = new TexturedModel(dragonmodel);

        Entity fruit = new Entity(texturedModel,
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

        List<Entity> mapEntities = new ArrayList<>();
        mapEntities.add(fruit);
        mapEntities.add(fruit2);
        mapEntities.add(stallEntity);
        mapEntities.add(dragon);

        Terrain terrain = new Terrain(0,0, loader, texture);

        System.out.println("Finished Loading");

        while (!Display.isCloseRequested()){
            long beforeMS = System.currentTimeMillis();

            //CAMERA
            camera.Move();
            camera.Rotate();

            //SCENE
            for (Entity entity: mapEntities){
                renderer.ProcessEntity(entity);
            }
            renderer.ProcessTerrain(terrain);

            //RENDERER
            renderer.Render(light, camera);

            //UPDATE FRAME
            DisplayManager.UpdateDisplay();

            //FPS STATS
            if (showFps){
                timePerFrame = (System.currentTimeMillis() - beforeMS);
                timePerFrame /= 1000;
                totalTime += timePerFrame;
                if (totalTime >= 1){
                    System.out.println(fps + " Fps - " + timePerFrame + " Ms");
                    fps = 0;
                    totalTime = 0;
                }
                else
                    fps++;
            }
        }

        renderer.CleanUp();
        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
