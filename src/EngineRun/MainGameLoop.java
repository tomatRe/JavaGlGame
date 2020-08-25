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
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainGameLoop {

    static final Vector3f lightColor = new Vector3f(1,1,1);
    static final Vector3f lightPosition = new Vector3f(50,25,50);
    static final boolean showFps = true;

    public static void main(String[] args){
        //ESSENTIALS
        DisplayManager.CreateDisplay();
        Loader loader = new Loader();
        Light light = new Light(lightPosition, lightColor);
        MasterRenderer renderer = new MasterRenderer();

        //MODELS
        RawModel fernModel = ObjLoader.LoadObjModel("fern", loader);
        RawModel grassModel = ObjLoader.LoadObjModel("grassModel", loader);
        RawModel treeModel = ObjLoader.LoadObjModel("lowPolyTree", loader);
        RawModel basicIcoModel = ObjLoader.LoadObjModel("basicIco", loader);

        //TEXTURES
        ModelTexture fernTexture = new ModelTexture(loader.LoadTexture("fern"));
        ModelTexture grassTexture = new ModelTexture(loader.LoadTexture("grassTexture"));
        ModelTexture treeTexture = new ModelTexture(loader.LoadTexture("lowPolyTree"));

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

        //SCENARY
        List<Entity> mapEntities = new ArrayList<>();
        Terrain terrain = new Terrain(0,0, loader, terrainTexturePack, terrainBlendmapTexture);
        int numOfFerns = 5000;
        int numOfTrees = 250;

        for (int i = 0; i < numOfFerns; i++){
            Random rnd = new Random();
            Vector3f pos = new Vector3f(rnd.nextFloat()*800, 0, rnd.nextFloat()*800);
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

        for (int i = 0; i < numOfTrees; i++){
            Random rnd = new Random();
            Vector3f pos = new Vector3f(rnd.nextFloat()*800, 0, rnd.nextFloat()*800);
            Vector3f rot = new Vector3f(0, rnd.nextFloat()*360, 0);
            Entity tree = new Entity(treeTextured, pos, rot, 1f);
            mapEntities.add(tree);
        }
        Entity lightIco = new Entity(icoTextured, lightPosition, new Vector3f(0,0,0), 1);

        //PLAYER
        Player player = new Player(icoTextured, new Vector3f(0,0,0), new Vector3f(0,0,0), 1);
        mapEntities.add(player);

        //CAMERA
        Camera camera = new Camera(player);

        System.out.println("Finished Loading");

        camera.SetRotation(new Vector3f(0,140,0));//LOOK BACK pls

        while (!Display.isCloseRequested()){
            //CAMERA
            camera.Move(DisplayManager.GetFrameTimeSeconds());
            camera.Rotate(DisplayManager.GetFrameTimeSeconds());

            //SCENE
            for (Entity entity: mapEntities){
                renderer.ProcessEntity(entity);
            }
            renderer.ProcessEntity(lightIco);
            renderer.ProcessTerrain(terrain);

            //PLAYER
            player.Move();

            //RENDERER
            renderer.Render(light, camera);

            //UPDATE FRAME
            DisplayManager.UpdateDisplay();
        }

        renderer.CleanUp();
        loader.CleanUp();
        DisplayManager.CloseDisplay();
    }
}
