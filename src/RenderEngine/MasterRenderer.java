package RenderEngine;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Models.TexturedModel;
import Shaders.StaticShader;
import Shaders.TerrainShader;
import Terrains.Terrain;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;
    private static final Vector4f SKY_COLOUR = new Vector4f(0.411764705f,0.776470588f,0.8f,1f);

    private Matrix4f projectionMatrix;

    private StaticShader shader = new StaticShader();
    private EntityRenderer entityRenderer;

    private TerrainRenderer terrainRenderer;
    private TerrainShader terrainShader = new TerrainShader();

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<>();

    public MasterRenderer() {
        EnableCulling();
        CreateProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
    }

    public static void EnableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void DisableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void Render(Light sun, Camera camera){
        float skyR = SKY_COLOUR.x;
        float skyG = SKY_COLOUR.y;
        float skyB = SKY_COLOUR.z;

        Prepare();

        terrainShader.Start();
        terrainShader.LoadSkyColour(skyR, skyG, skyB);
        terrainShader.LoadLight(sun);
        terrainShader.LoadViewMatrix(camera);
        terrainRenderer.Render(terrains);
        terrainShader.Stop();

        shader.Start();
        shader.LoadSkyColour(skyR, skyG, skyB);
        shader.LoadLight(sun);
        shader.LoadViewMatrix(camera);
        entityRenderer.Render(entities);
        shader.Stop();

        terrains.clear();
        entities.clear();
    }

    public void ProcessEntity(Entity entity){
        TexturedModel entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        if (batch != null){
            batch.add(entity);
        }
        else{
            List<Entity> newBatch = new ArrayList<Entity>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public void ProcessTerrain(Terrain terrain){
        terrains.add(terrain);
    }

    public void Prepare(){
        float skyR = SKY_COLOUR.x;
        float skyG = SKY_COLOUR.y;
        float skyB = SKY_COLOUR.z;
        float skyA = SKY_COLOUR.w;

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(skyR, skyG, skyB, skyA);//BACKGROUND COLOR
    }

    private void CreateProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f/Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale/aspectRatio;
        float frustum_lenght = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE+NEAR_PLANE) / frustum_lenght);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE)/frustum_lenght);
        projectionMatrix.m33 = 0;
    }

    public void CleanUp(){
        shader.CleanUp();
        terrainShader.CleanUp();
    }
}
