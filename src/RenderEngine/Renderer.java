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

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projectionMatrix;
    public StaticShader shader;

    public Renderer(StaticShader shader) {
        this.shader = shader;

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);

        CreateProjectionMatrix();
        this.shader.Start();
        this.shader.LoadProjectionMatrix(projectionMatrix);
        this.shader.Stop();
    }

    public void Prepare(){
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0.2f,0.2f,0.2f,1f);//BACKGROUND COLOR
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
        shader.LoadShineVariables(texture.getShineDumper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());
    }

    private void UnbindTexturedModel(){
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
/* OLD RENDERER
    public void Render(Entity entity, StaticShader shader){
        TexturedModel texturedModel = entity.getModel();
        RawModel model = texturedModel.getRawModel();

        GL30.glBindVertexArray(model.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        Matrix4f transformationMatrix =
                Maths.createTransofrmationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.LoadTransformationMatrix(transformationMatrix);

        ModelTexture texture = texturedModel.getTexture();
        shader.LoadShineVariables(texture.getShineDumper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());

        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }*/

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

}
