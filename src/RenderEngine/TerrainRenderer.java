package RenderEngine;

import Models.RawModel;
import Shaders.TerrainShader;
import Terrains.Terrain;
import Textures.TerrainTexturePack;
import ToolBox.Maths;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

public class TerrainRenderer {

    private TerrainShader shader;

    public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        shader.Start();
        shader.LoadProjectionMatrix(projectionMatrix);
        shader.ConnectTextureUnits();
        shader.Stop();
    }

    public void Render(List<Terrain> terrains){
        for (Terrain terrain: terrains){
            PrepareTerrain(terrain);
            LoadModelMatrix(terrain);

            GL11.glDrawElements(GL11.GL_TRIANGLES,
                    terrain.getModel().getVertexCount(),
                    GL11.GL_UNSIGNED_INT, 0);

            UnbindTexturedModel();
        }
    }

    private void PrepareTerrain(Terrain terrain){
        RawModel rawModel = terrain.getModel();

        GL30.glBindVertexArray(rawModel.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        BindTextures(terrain);
        shader.LoadShineVariables(1, 0);//TODO
    }

    private void BindTextures(Terrain terrain){
        TerrainTexturePack terrainTexturePack = terrain.getTexturePack();

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getBackgroundTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getrTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getgTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainTexturePack.getbTexture().getTextureID());

        GL13.glActiveTexture(GL13.GL_TEXTURE4);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());


    }

    private void UnbindTexturedModel(){
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void LoadModelMatrix(Terrain terrain){
        Vector3f position = new Vector3f(terrain.getX(), 0, terrain.getZ());
        Vector3f rotation = new Vector3f(0,0,0);
        float scale = 1;

        Matrix4f transformationMatrix =
                Maths.createTransofrmationMatrix(position, rotation, scale);
        shader.LoadTransformationMatrix(transformationMatrix);
    }
}
