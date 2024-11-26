package EngineRun;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Models.TexturedModel;
import RenderEngine.*;
import Models.RawModel;
import Terrains.Terrain;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import guis.GuiRenderer;
import guis.GuiTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    static final Vector3f sunColour = new Vector3f(1.5f,1.5f,1f);
    static final Vector3f sunPosition = new Vector3f(1400,1500,1400);

    private static List<Light> GenerateRandomLights(List<Light> lights){
        for (int i = 0; i < 60; i++){
            Random rnd = new Random();
            Vector3f position = new Vector3f
                    (rnd.nextFloat()*800, 5, rnd.nextFloat()*800);

            Vector3f colour = new Vector3f
                    (rnd.nextFloat()*1000, rnd.nextFloat()*1000, rnd.nextFloat()*1000);

            Vector3f attenuation = new Vector3f
                    (rnd.nextFloat()*50,rnd.nextFloat()*50,rnd.nextFloat()*50);

            Light light = new Light(position, colour, attenuation);
            lights.add(light);
        }

        return lights;
    }

    private static Terrain GenerateEntities(Loader loader, List<Entity> mapEntities){
        int numOfFerns = 5000;
        int numOfTrees = 250;

        //MODELS
        RawModel fernModel = ObjLoader.LoadObjModel("fern", loader);
        RawModel grassModel = ObjLoader.LoadObjModel("grassModel", loader);
        RawModel treeModel = ObjLoader.LoadObjModel("highPolyTree", loader);
        RawModel basicIcoModel = ObjLoader.LoadObjModel("basicIco", loader);

        //TEXTURES
        ModelTexture fernTexture = new ModelTexture(loader.LoadTexture("fern"));
        ModelTexture grassTexture = new ModelTexture(loader.LoadTexture("grassTexture"));
        ModelTexture treeTexture = new ModelTexture(loader.LoadTexture("grassTexture"));

        //TERRAIN TEXTURES
        TerrainTexture terrainBackgroundTexture = new TerrainTexture(loader.LoadTexture("grass"));
        TerrainTexture terrainRTexture = new TerrainTexture(loader.LoadTexture("grassFlowers"));
        TerrainTexture terrainGTexture = new TerrainTexture(loader.LoadTexture("mud"));
        TerrainTexture terrainBTexture = new TerrainTexture(loader.LoadTexture("path"));
        TerrainTexture terrainBlendmapTexture = new TerrainTexture(loader.LoadTexture("blendmap"));
        TerrainTexturePack terrainTexturePack = new TerrainTexturePack
                (terrainBackgroundTexture, terrainRTexture, terrainGTexture, terrainBTexture);

        //TEXTURED MODELS
        TexturedModel ferntextured = new TexturedModel(fernModel,fernTexture);
        TexturedModel grassTextured = new TexturedModel(grassModel, grassTexture);
        TexturedModel treeTextured = new TexturedModel(treeModel, treeTexture);
        TexturedModel icoTextured = new TexturedModel(basicIcoModel);

        ferntextured.getTexture().setUseFakeLightning(true);
        ferntextured.getTexture().setHasTransparecy(true);
        grassTextured.getTexture().setUseFakeLightning(true);
        grassTextured.getTexture().setHasTransparecy(true);
        icoTextured.getTexture().setUseFakeLightning(true);
        treeTextured.getTexture().setHasTransparecy(true);
        treeTextured.getTexture().setUseFakeLightning(false);

        Terrain terrain = new Terrain(0,0, loader, terrainTexturePack, terrainBlendmapTexture, "heightmap");

        for (int i = 0; i < numOfFerns; i++){//SPAWN GRASS
            Random rnd = new Random();
            float posx = rnd.nextFloat()*800;
            float posz = rnd.nextFloat()*800;
            Vector3f pos = new Vector3f(posx, terrain.getHeight(posx,posz), posz);
            Vector3f rot = new Vector3f(0, rnd.nextFloat()*360, 0);

            if (i%2 == 0){
                Entity fern = new Entity(ferntextured, pos, rot, 1f);
                mapEntities.add(fern);
            }
            else{
                Entity grass = new Entity(grassTextured,pos,rot, 1f);
                mapEntities.add(grass);
            }
        }

        for (int i = 0; i < numOfTrees; i++){//SPAWN TREES
            Random rnd = new Random();
            float scale = rnd.nextFloat()+2;
            float posx = rnd.nextFloat()*800;
            float posz = rnd.nextFloat()*800;
            Vector3f pos = new Vector3f(posx, terrain.getHeight(posx, posz), posz);
            Vector3f rot = new Vector3f(0, rnd.nextFloat()*360, 0);
            Entity tree = new Entity(treeTextured, pos, rot, scale);
            mapEntities.add(tree);
        }
        Entity lightIco = new Entity(icoTextured, sunPosition, new Vector3f(0,0,0), 1);
        mapEntities.add(lightIco);

        return terrain;
    }

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

        //LIGHTS
        List<Light> lights = new ArrayList<>();
        Light sun = new Light(sunPosition, sunColour);
        //GenerateRandomLights(lights);
        lights.add(0,sun);

        //SCENARY
        List<Entity> mapEntities = new ArrayList<>();
        Terrain terrain;
        terrain = GenerateEntities(loader, mapEntities);

        //PLAYER
        RawModel basicIcoModel = ObjLoader.LoadObjModel("basicIco", loader);
        TexturedModel icoTextured = new TexturedModel(basicIcoModel);
        icoTextured.getTexture().setUseFakeLightning(true);
        Player player = new Player(icoTextured, new Vector3f(0,0,0), new Vector3f(0,0,0), 1);
        //mapEntities.add(player);

        //CAMERA
        Camera camera = new Camera();

        //HUD AND GUIS
        List<GuiTexture> guis = new ArrayList<>();
        GuiTexture hudTest = new GuiTexture(
                loader.LoadTexture("deftext"), new Vector2f(-0.70f,0.85f), new Vector2f(0.25f,0.01f));
        //guis.add(hudTest);

        //FINISHED LOADING
        System.out.println("Finished Loading...");
        System.out.println("Entities: " + mapEntities.size());
        System.out.println("Lights: " + lights.size());

        while (!Display.isCloseRequested()){

            //SCENE
            for (Entity entity: mapEntities)
                renderer.ProcessEntity(entity);
            renderer.ProcessTerrain(terrain);

            //CAMERA
            camera.Move(DisplayManager.GetFrameTimeSeconds());

            //PLAYER
            player.Move(terrain);

            //RENDERER
            renderer.Render(lights, camera);

            //GUIS
            guiRenderer.Render(guis);

            //UPDATE FRAME
            DisplayManager.UpdateDisplay();
        }

        CleanUp(renderer, guiRenderer, loader);
    }
}
