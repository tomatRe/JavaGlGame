package Skybox;

import Entities.Camera;
import Models.RawModel;
import RenderEngine.DisplayManager;
import RenderEngine.Loader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SkyboxRenderer {

    private static final float SIZE = 500f;
    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    private static String[] TEXTURE_FILES = {
            "right", "left", "top", "bottom", "back", "front"
    };
    private static String[] NIGHT_TEXTURE_FILES = {
            "nightRight", "nightLeft", "nightTop", "nightBottom", "nightBack", "nightFront"
    };

    private RawModel cube;
    private int dayTexture;
    private int nightTexture;
    private SkyboxShader shader;
    private boolean dayNightCycleEnabled = false;
    private float time = 0;
    private int dayTime = 5;
    private int nightTime = 5;

    public SkyboxRenderer(Loader loader, Matrix4f projextionMatrix){
        cube = loader.LoadtoVAO(VERTICES, 3);
        dayTexture = loader.LoadCubemap(TEXTURE_FILES);
        //nightTexture = loader.LoadCubemap(NIGHT_TEXTURE_FILES);
        shader = new SkyboxShader();
        shader.Start();
        shader.ConnectTextureUnits();
        shader.LoadProjectionMatrix(projextionMatrix);
        shader.Stop();
    }

    public void Render(Camera camera, Vector3f fogColour){
        shader.Start();
        shader.LoadViewMatrix(camera);
        shader.LoadFogColour(fogColour);

        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);

        if (dayNightCycleEnabled)
            BindTextures();

        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.Stop();
    }

    private void BindTextures(){
        time += DisplayManager.GetFrameTimeSeconds() * 1;
        time %= 24000;
        int texture1;
        int texture2;
        float blendFactor;

        if(time >= 0 && time < dayTime){
            texture1 = nightTexture;
            texture2 = nightTexture;
            blendFactor = (time - 0)/(dayTime);
        }else if(time >= dayTime && time < nightTime){
            texture1 = nightTexture;
            texture2 = dayTexture;
            blendFactor = (time - dayTime)/(nightTime - dayTime);
        }else if(time >= nightTime && time < 21000){
            texture1 = dayTexture;
            texture2 = dayTexture;
            blendFactor = (time - nightTime)/(21000 - nightTime);
        }else{
            texture1 = dayTexture;
            texture2 = nightTexture;
            blendFactor = (time - 21000)/(24000 - 21000);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.LoadBlendFactor(blendFactor);
    }
}
