package RenderEngine;

import Entities.Entity;
import Models.RawModel;
import Models.TextureModel;
import Shaders.StaticShader;
import ToolBox.Maths;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Matrix4f;

public class Renderer {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000f;

    private Matrix4f projectionMatrix;

    public Renderer(StaticShader shader) {
        CreateProjectionMatrix();
        shader.Start();
        shader.LoadProjectionMatrix(projectionMatrix);
        shader.Stop();
    }

    public void Prepare(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        GL11.glClearColor(0.2f,0.2f,0.2f,1f);//BACKGROUND COLOR
    }

    public void Render(Entity entity, StaticShader shader){
        TextureModel textureModel = entity.getModel();
        RawModel model = textureModel.getRawModel();

        GL30.glBindVertexArray(model.getVaoID());

        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f transformationMatrix =
                Maths.createTransofrmationMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
        shader.LoadTransformationMatrix(transformationMatrix);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureModel.getTexture().getID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
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

}
