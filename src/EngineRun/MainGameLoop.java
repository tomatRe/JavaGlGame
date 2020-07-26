package EngineRun;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Models.TexturedModel;
import RenderEngine.*;
import Models.RawModel;
import Terrains.Terrain;
import Textures.ModelTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        RawModel fernModel = ObjLoader.LoadObjModel("fern", loader);
        RawModel grassModel = ObjLoader.LoadObjModel("grassModel", loader);

        //TEXTURES
        ModelTexture terrainTexture = new ModelTexture(loader.LoadTexture("grass"));
        ModelTexture fernTexture = new ModelTexture(loader.LoadTexture("fern"));
        ModelTexture grassTexture = new ModelTexture(loader.LoadTexture("grassTexture"));
        terrainTexture.setShineDumper(10);
        terrainTexture.setReflectivity(1);

        //TEXTURED MODELS
        TexturedModel ferntextured = new TexturedModel(fernModel,fernTexture);
        TexturedModel grassTextured = new TexturedModel(grassModel, grassTexture);

        ferntextured.getTexture().setUseFakeLightning(true);
        grassTextured.getTexture().setUseFakeLightning(true);

        //SCENARY
        List<Entity> mapEntities = new ArrayList<>();
        Terrain terrain = new Terrain(0,0, loader, terrainTexture);
        int numOfFerns = 5000;

        for (int i = 0; i < numOfFerns; i++){

            Random rnd = new Random();

            Vector3f pos = new Vector3f(rnd.nextFloat()*100, 0, rnd.nextFloat()*100);
            Vector3f rot = new Vector3f(0, /*rnd.nextFloat()*36*/0, 0);

            if (i%2 == 0){
                Entity fern = new Entity(ferntextured, pos, rot, 0.05f);
                mapEntities.add(fern);
            }
            else{
                Entity grass = new Entity(grassTextured,pos,rot, 0.1f);
                mapEntities.add(grass);
            }
        }

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
