package RenderEngine;

import Entities.Entity;
import Models.RawModel;
import Models.TexturedModel;
import Shaders.StaticShader;
import Textures.ModelTexture;
import ToolBox.Maths;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

import java.util.List;
import java.util.Map;

public class EntityRenderer {
    public StaticShader shader;

    public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
        this.shader = shader;
        this.shader.Start();
        this.shader.LoadProjectionMatrix(projectionMatrix);
        this.shader.Stop();
    }

    public void Render(Map<TexturedModel, List<Entity>> entities){
        for (TexturedModel model:entities.keySet()){
            PrepareTexturedModel(model);
            List<Entity> batch = entities.get(model);

            for (Entity entity:batch){
                PrepareInstance(entity);

                GL11.glDrawElements(GL11.GL_TRIANGLES,
                        model.getRawModel().getVertexCount(),
                        GL11.GL_UNSIGNED_INT, 0);
            }

            UnbindTexturedModel();
        }
    }

    private void PrepareTexturedModel(TexturedModel model){
        RawModel rawModel = model.getRawModel();

        GL30.glBindVertexArray(rawModel.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        ModelTexture texture = model.getTexture();

        if (texture.hasTransparecy()){
            MasterRenderer.DisableCulling();
        }

        shader.LoadFakeLightningVariable(texture.usingFakeLightning());
        shader.LoadShineVariables(texture.getShineDumper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }

    private void UnbindTexturedModel(){
        MasterRenderer.EnableCulling();

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void PrepareInstance(Entity entity){
        Matrix4f transformationMatrix =
                Maths.createTransofrmationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.LoadTransformationMatrix(transformationMatrix);
    }

}
