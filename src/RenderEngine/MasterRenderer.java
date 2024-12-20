package RenderEngine;

import Entities.Camera;
import Entities.Entity;
import Entities.Light;
import Models.TexturedModel;
import Shaders.StaticShader;
import Shaders.TerrainShader;
import Skybox.SkyboxRenderer;
import Terrains.Terrain;
import Water.WaterRenderer;
import Water.WaterShader;
import Water.WaterTile;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MasterRenderer {

    private static final float FOV = 90;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projectionMatrix;
    private Vector4f FOG_COLOUR = new Vector4f(0.8f,0.9f,0.9f,1f);

    private StaticShader shader = new StaticShader();
    private TerrainShader terrainShader = new TerrainShader();
    private WaterShader waterShader = new WaterShader();

    private EntityRenderer entityRenderer;
    private TerrainRenderer terrainRenderer;
    private WaterRenderer waterRenderer;
    private SkyboxRenderer skyboxRenderer;

    private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
    private List<Terrain> terrains = new ArrayList<>();
    private List<WaterTile> water = new ArrayList<>();

    public MasterRenderer(Loader loader) {
        EnableCulling();
        EnableClipDistance();
        CreateProjectionMatrix();
        entityRenderer = new EntityRenderer(shader, projectionMatrix);
        terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
        waterRenderer = new WaterRenderer(loader, waterShader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
    }

    public static void EnableCulling(){
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void EnableClipDistance(){
        GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
    }

    public static void DisableCulling(){
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void Render(List<Light> lights, Camera camera){
        float skyR = FOG_COLOUR.x;
        float skyG = FOG_COLOUR.y;
        float skyB = FOG_COLOUR.z;

        Prepare();
        SortLights(lights, camera.getPosition());

        terrainShader.Start();
        terrainShader.LoadSkyColour(skyR, skyG, skyB);
        terrainShader.LoadLights(lights);
        terrainShader.LoadViewMatrix(camera);
        terrainRenderer.Render(terrains);
        terrainShader.Stop();

        shader.Start();
        shader.LoadSkyColour(skyR, skyG, skyB);
        shader.LoadLights(lights);
        shader.LoadViewMatrix(camera);
        entityRenderer.Render(entities);
        shader.Stop();

        waterShader.Start();
        waterRenderer.Render(water, camera);
        waterShader.Stop();

        skyboxRenderer.Render(camera, new Vector3f(skyR, skyG, skyB));

        water.clear();
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

    public void ProcessWater(WaterTile waterTile){
        water.add(waterTile);
    }

    public void Prepare(){
        float skyR = FOG_COLOUR.x;
        float skyG = FOG_COLOUR.y;
        float skyB = FOG_COLOUR.z;
        float skyA = FOG_COLOUR.w;

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

    private static List<Light> SortLights(List<Light> lights, Vector3f cameraPos){

        Light sun = lights.get(0);
        lights.remove(0);

        lights.sort((Light1, Light2) -> {
            Vector3f dist1V = new Vector3f();
            Vector3f dist2V = new Vector3f();

            Vector3f.sub(Light1.getPosition(), cameraPos, dist1V);
            Vector3f.sub(Light2.getPosition(), cameraPos, dist2V);

            float Dist1 = dist1V.length();
            float Dist2 = dist2V.length();

            return Float.compare(Dist1, Dist2);
        });

        lights.add(0, sun);

        return lights;
    }

    public void CleanUp(){
        waterShader.CleanUp();
        shader.CleanUp();
        terrainShader.CleanUp();
    }

    public Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

    public void setFogColour(Vector4f newColour){
        this.FOG_COLOUR = newColour;
    }
}
