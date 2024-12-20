package Level;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Entities.Player;
import Models.RawModel;
import Models.TexturedModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import RenderEngine.MasterRenderer;
import RenderEngine.ObjLoader;
import Terrains.Terrain;
import Textures.ModelTexture;
import Textures.TerrainTexture;
import Textures.TerrainTexturePack;
import Water.WaterFrameBuffers;
import Water.WaterTile;
import guis.GuiRenderer;
import guis.GuiTexture;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ForestLevel implements Level {

    // Needed entities
    Camera camera;
    Loader loader;
    MasterRenderer renderer;
    GuiRenderer guiRenderer;

    // Level content
    List<Entity> mapEntities;
    List<Light> lights;
    List<Player> players;
    List<GuiTexture> guis;
    List<Terrain> terrains;
    List<WaterTile> waterTiles;

    // Reflections
    WaterFrameBuffers wfb;
    GuiTexture reflection;

    // Make it pretty
    static final Vector3f sunColour = new Vector3f(1.5f,1.5f,1f);
    static final Vector3f sunPosition = new Vector3f(1400,1500,1400);

    Vector4f FOG_COLOUR = new Vector4f(0.8f,0.9f,0.9f,1f);

    public ForestLevel(Loader loader, MasterRenderer renderer, GuiRenderer guiRenderer){
        this.loader = loader;
        this.renderer = renderer;
        this.guiRenderer = guiRenderer;

        camera = new Camera();
        mapEntities = new ArrayList<>();
        lights = new ArrayList<>();
        players = new ArrayList<>();
        guis = new ArrayList<>();
        terrains = new ArrayList<>();
        waterTiles = new ArrayList<>();

        Light sun = new Light(sunPosition, sunColour);
        lights.add(0,sun);
    }

    public void GenerateEntities(){
        int numOfFerns = 5000;
        int numOfTrees = 250;

        System.out.println("Level Generation began!");

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
        terrains.add(terrain);

        //PLAYER
        icoTextured.getTexture().setUseFakeLightning(true);
        Player player = new Player(icoTextured, new Vector3f(0,0,0), new Vector3f(0,0,0), 1);
        //camera.setPlayer(player);
        //players.add(player);

        //Water
        WaterTile water = new WaterTile(200, 200, 0);
        water.SetTileSize(200);
        waterTiles.add(water);

        //Water reflection buffer
        wfb = new WaterFrameBuffers();
        reflection = new GuiTexture(wfb.getReflectionTexture(), new Vector2f(-0.75f, 0.75f), new Vector2f(0.25f, 0.25f));
        guis.add(reflection);

        renderer.setFogColour(FOG_COLOUR);

        System.out.println("Level Generation ended!");
    }

    public void LoadLevel(){
        GenerateEntities();

        //FINISHED LOADING
        System.out.println("Finished Loading...");
        System.out.println("Entities: " + mapEntities.size());
        System.out.println("Lights: " + lights.size());
        System.out.println("Players: " + players.size());
        System.out.println("Guis: " + guis.size());
        System.out.println("Terrains: " + terrains.size());
        System.out.println("Water Plains: " + waterTiles.size());
    }

    public void EventTick(){
        UpdateEntities();
        UpdatePlayers();
        UpdateCamera();
    }

    public void RenderLevel(){
        RenderTerrains();
        RenderEntities();
        RenderPlayers();
        renderer.Render(lights, camera);

        RenderWater();
        guiRenderer.Render(guis);
    }

    public void RenderTerrains(){
        for (Terrain terrain: terrains)
            renderer.ProcessTerrain(terrain);
    }

    public void RenderEntities(){
        for (Entity entity: mapEntities)
            renderer.ProcessEntity(entity);
    }

    public void RenderPlayers(){
        for (Player player: players)
            renderer.ProcessEntity(player);
    }

    @Override
    public void RenderWater() {
        wfb.bindReflectionFrameBuffer();
        RenderTerrains();
        RenderEntities();
        RenderPlayers();
        renderer.Render(lights, camera);
        wfb.unbindCurrentFrameBuffer();

        for (WaterTile waterTile: waterTiles)
            renderer.ProcessWater(waterTile);
    }

    public void UpdatePlayers(){
        for (Player player: players)
            player.Move(terrains.get(0));

        // TODO add support for multiple terrain collision
    }

    public void UpdateEntities(){
        // TODO add tick for movable entities
    }

    public void UpdateCamera(){
        camera.Move(DisplayManager.GetFrameTimeSeconds());
    }

    @Override
    public void CleanUp() {
        loader.CleanUp();
        wfb.cleanUp();
        guiRenderer.CleanUp();
        renderer.CleanUp();

        camera = null;
        mapEntities = null;
        lights = null;
        players = null;
        guis = null;
        terrains = null;
        waterTiles = null;
    }


}
